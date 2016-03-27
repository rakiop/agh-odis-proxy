# agh-odis-proxy

## Build
To build run command:
```bash
mvn clean install
```

All files will be in [basedir]\target

## IntelliJ Configuration
To properly build add new maven goal:
```
install
```
To do speed build and run change this goal to:
```
install -DskipTests
```

## Examle configuration file
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configurations>
    <configuration type="HTTP_SERVER" port="8080">
        <filter filterName="pl.edu.agh.weaiib.is.odis.proxy.plugins.BlackListUrlsPlugin" place="SERVER_CLIENT_TO_PROXY" priority="1" timeFrom="00:00" timeTo="23:59:59">
            <parameter>
                <key>list</key>
                <value class="pl.edu.agh.weaiib.is.odis.proxy.SerializableList">
                    <item>google.pl</item>
                </value>
            </parameter>
        </filter>
    </configuration>
</configurations>
```

## Run
To start this proxy type in [base]\target:
```
java -jar ODiS-Proxy-1.0-SNAPSHOT.jar
```
