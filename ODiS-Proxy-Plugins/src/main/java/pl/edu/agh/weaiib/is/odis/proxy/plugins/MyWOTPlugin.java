package pl.edu.agh.weaiib.is.odis.proxy.plugins;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.weaiib.is.odis.proxy.SerializableList;
import pl.edu.agh.weaiib.is.odis.proxy.helpers.DateHelper;
import pl.edu.agh.weaiib.is.odis.proxy.helpers.URIHelper;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Plugin to drop request that use Web of Trust to filter domains
 */
public class MyWOTPlugin extends ODiSHttpFilter  {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MyWOTPlugin.class);

    /**
     * Web of Trust API for *MyWOT*
     */
    private static final String URL = "http://api.mywot.com/0.4/public_link_json2?hosts=%s/&key=%s";

    /**
     * API access key
     */
    private static final  String APIKey = "6fe3705f9994c1cdaa37ac44555cc3d590d96f71";
    /**
     * Cache time for domain status (12H)
     */
    private static final int trustTime = 12*60*60*1000;

    /**
     * Level that domain suits category
     */
    private static final int blockCategoryMatchLevel = 40;

    /**
     * Map with domain statuses cache
     */
    private static ConcurrentHashMap<String, DomainStatus> statuses = new ConcurrentHashMap<>();

    /**
     * Configuration property that says that domain is safe (501 category)
     */
    private int trustLevel = 60;

    /**
     * Categories that will block request
     */
    private List<String> blockCategories = new LinkedList<>();

    /**
     * If domain reputation is not found do we block request?
     */
    private boolean blockIfNotFound = true;

    /**
     * Test request
     * @param originalRequest   Request Request object
     * @param ctx               Contest Request context
     * @return                          Is request safe?
     */
    @Override
    public FilterResponse testHttpRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        String uri = originalRequest.getUri();
        String host = URIHelper.getHost(uri);

        if(!statuses.containsKey(host) || DateHelper.differenceIsMoreThen(new Date(), statuses.get(host).CheckDate, trustTime)){
            int level = callWoTAPIService(host);
            boolean isSave = (level == -1 && !blockIfNotFound) || level > trustLevel;
            DomainStatus status = new DomainStatus(isSave, level);

            statuses.put(host, status);
        }

        DomainStatus stat = statuses.get(host);
        if(stat != null && stat.IsSafe){
            return new FilterResponse(true);
        }
        else
            return new FilterResponse(false, String.format("This domain has poor reputation: %s/100.", stat.Level));
    }

    /**
     * Skip filter response
     * @param originalRequest   Request object
     * @param response          Response object - aggregated
     * @param content           Parsed content to text format
     * @return                  Always true
     */
    @Override
    public FilterResponse testHttpResponse(HttpRequest originalRequest, HttpObject response, String content) {
        return new FilterResponse(true);
    }

    /**
     * Read plugin properties
     */
    @Override
    public void init() {
        Object trustLevelObject = parameters.get("trustLevel");
        if(trustLevelObject instanceof Integer){
            trustLevel = (int)trustLevelObject;
        }

        Object blockedCategoriesObject = parameters.get("blockCategories");
        if(blockedCategoriesObject instanceof SerializableList){
            SerializableList propertyList = (SerializableList)blockedCategoriesObject;
            blockCategories = new LinkedList<>();
            for(String category : propertyList.getList()){
                String trimmedCategory = category.trim();
                blockCategories.add(trimmedCategory);
            }
        }

        Object blockIfNotFoundObject = parameters.get("blockIfNotFound");
        if(blockIfNotFoundObject instanceof Boolean){
            blockIfNotFound = (boolean)blockIfNotFoundObject;
        }
    }

    /**
     * Calling API to check domain reputation
     * @param host  domain address
     * @return      domain reputation level
     */
    private int callWoTAPIService(String host){
        String url = String.format(URL, host, APIKey);

        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        HttpMethod method = new GetMethod(url);
        method.setFollowRedirects(true);
        String responseBody = "";
        try{
            client.executeMethod(method);
            responseBody = method.getResponseBodyAsString();
        } catch (HttpException he) {
            LOGGER.warn(String.format("HTTP error %s", he.getMessage()));
        } catch (IOException ioe){
            LOGGER.warn("Unable to connect to WoT API");
        }

        if(responseBody.isEmpty()){
            return -1;
        }

        try {
            HashMap<String,Object> object = new ObjectMapper().readValue(responseBody, HashMap.class);

            if(object.containsKey(host)){
                HashMap<String, Object> domainData = (HashMap<String, Object>) object.get(host);
                if(domainData.containsKey("categories")){
                    HashMap<String, Object> categoriesData = (HashMap<String, Object>) domainData.get("categories");

                    for(String blockCat : blockCategories){
                        if(categoriesData.containsKey(blockCat)){
                            int lvl = (int)categoriesData.get(blockCat);
                            if(lvl > blockCategoryMatchLevel)
                                return 0;
                        }
                    }

                    if(categoriesData.containsKey("501")){
                        return (int)categoriesData.get("501");
                    }
                    else{
                        return (int)(categoriesData.values().toArray()[0]);
                    }
                }
                else if(domainData.containsKey("0")){
                    HashMap<String, Object> generalData = (HashMap<String, Object>) domainData.get("0");
                    return (int) (generalData.values().toArray()[0]);
                }
                else{
                    LOGGER.warn("Weird, reputation info not found");
                    return -1;
                }
            }

        } catch (IOException e) {
            LOGGER.warn("Deserialization error");
            return -1;
        }

        return -1;
    }

    /**
     * Inner class for domain reputation
     */
    private class DomainStatus{

        /**
         * Can process with this domain?
         */
        public boolean IsSafe;

        /**
         * Cache date
         */
        public Date CheckDate;

        /**
         * Save level
         */
        public int Level;

        /**
         * Constructor
         * @param isSafe    Is domain save
         * @param level     Reputation level
         */
        public DomainStatus(boolean isSafe, int level){
            IsSafe = isSafe;
            Level = level;
            CheckDate = new Date();
        }
    }

}
