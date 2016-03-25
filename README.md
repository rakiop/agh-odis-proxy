# agh-odis-proxy

## Build
To build run command:
```bash
mvn clean install
```

All files will be in [basedir]\target

## IntelliJ Configuration
To build add new maven goal:
```
install
```

## Examle configuration file
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configurations>
    <configurations>
        <filters>
            <filter>
                <class>pl.edu.agh.weaiib.is.odis.proxy.plugins.BlackListUrlsPlugin</class>
                <parameters>
                    <entry>
                        <key>list</key>
                        <value>www.google.pl;www.coachingsport.pl</value>
                    </entry>
                </parameters>
                <place>SERVER_CLIENT_TO_PROXY</place>
                <priority>1</priority>
            </filter>
        </filters>
        <port>8099</port>
        <timeFrom>23:59</timeFrom>
        <timeTo>00:00</timeTo>
        <type>HTTP_SERVER</type>
    </configurations>
</configurations>
```
