@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  java-sdk-examples startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and JAVA_SDK_EXAMPLES_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\java-sdk-examples-1.0-SNAPSHOT.jar;%APP_HOME%\lib\orkes-conductor-client-1.0.4.jar;%APP_HOME%\lib\conductor-client-3.9.0-orkes-g.jar;%APP_HOME%\lib\conductor-common-3.9.0-orkes-g.jar;%APP_HOME%\lib\conductor-annotations-3.9.0-orkes-g.jar;%APP_HOME%\lib\log4j-web-2.17.1.jar;%APP_HOME%\lib\log4j-core-2.17.1.jar;%APP_HOME%\lib\log4j-jul-2.17.1.jar;%APP_HOME%\lib\log4j-to-slf4j-2.17.1.jar;%APP_HOME%\lib\log4j-api-2.17.1.jar;%APP_HOME%\lib\slf4j-simple-1.7.36.jar;%APP_HOME%\lib\spectator-api-1.3.7.jar;%APP_HOME%\lib\eureka-client-1.10.17.jar;%APP_HOME%\lib\netflix-eventbus-0.3.0.jar;%APP_HOME%\lib\archaius-core-0.7.6.jar;%APP_HOME%\lib\servo-core-0.12.21.jar;%APP_HOME%\lib\azure-security-keyvault-secrets-4.2.3.jar;%APP_HOME%\lib\azure-identity-1.3.7.jar;%APP_HOME%\lib\azure-core-http-netty-1.11.1.jar;%APP_HOME%\lib\azure-core-1.21.0.jar;%APP_HOME%\lib\msal4j-persistence-extension-1.1.0.jar;%APP_HOME%\lib\msal4j-1.11.0.jar;%APP_HOME%\lib\netflix-infix-0.3.0.jar;%APP_HOME%\lib\slf4j-api-1.7.36.jar;%APP_HOME%\lib\commons-lang3-3.12.0.jar;%APP_HOME%\lib\jackson-jaxrs-json-provider-2.11.4.jar;%APP_HOME%\lib\jackson-module-jaxb-annotations-2.13.4.jar;%APP_HOME%\lib\jackson-dataformat-xml-2.13.4.jar;%APP_HOME%\lib\jackson-annotations-2.13.4.jar;%APP_HOME%\lib\jackson-jaxrs-base-2.13.4.jar;%APP_HOME%\lib\aws-java-sdk-ssm-1.12.300.jar;%APP_HOME%\lib\aws-java-sdk-core-1.12.300.jar;%APP_HOME%\lib\jackson-dataformat-cbor-2.13.4.jar;%APP_HOME%\lib\jackson-core-2.13.4.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.11.4.jar;%APP_HOME%\lib\jmespath-java-1.12.300.jar;%APP_HOME%\lib\jackson-databind-2.13.4.jar;%APP_HOME%\lib\jersey-apache-client4-1.19.1.jar;%APP_HOME%\lib\jersey-client-1.19.4.jar;%APP_HOME%\lib\logging-interceptor-2.7.5.jar;%APP_HOME%\lib\okhttp-2.7.5.jar;%APP_HOME%\lib\gson-fire-1.8.5.jar;%APP_HOME%\lib\gson-2.9.0.jar;%APP_HOME%\lib\swagger-annotations-2.2.2.jar;%APP_HOME%\lib\threetenbp-1.6.1.jar;%APP_HOME%\lib\jersey-core-1.19.4.jar;%APP_HOME%\lib\xstream-1.4.18.jar;%APP_HOME%\lib\jsr311-api-1.1.1.jar;%APP_HOME%\lib\httpclient-4.5.13.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\commons-configuration-1.10.jar;%APP_HOME%\lib\guice-4.1.0.jar;%APP_HOME%\lib\compactmap-2.0.jar;%APP_HOME%\lib\jettison-1.4.0.jar;%APP_HOME%\lib\okio-1.6.0.jar;%APP_HOME%\lib\jna-platform-5.6.0.jar;%APP_HOME%\lib\oauth2-oidc-sdk-9.7.jar;%APP_HOME%\lib\json-smart-2.4.7.jar;%APP_HOME%\lib\commons-io-2.7.jar;%APP_HOME%\lib\bval-jsr-2.0.5.jar;%APP_HOME%\lib\protobuf-java-3.13.0.jar;%APP_HOME%\lib\commons-math-2.2.jar;%APP_HOME%\lib\mxparser-1.2.2.jar;%APP_HOME%\lib\guava-19.0.jar;%APP_HOME%\lib\httpcore-4.4.13.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-codec-1.15.jar;%APP_HOME%\lib\commons-lang-2.6.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\aopalliance-1.0.jar;%APP_HOME%\lib\dexx-collections-0.2.jar;%APP_HOME%\lib\ion-java-1.0.2.jar;%APP_HOME%\lib\joda-time-2.8.1.jar;%APP_HOME%\lib\reactor-netty-http-1.0.11.jar;%APP_HOME%\lib\reactor-netty-core-1.0.11.jar;%APP_HOME%\lib\reactor-core-3.4.10.jar;%APP_HOME%\lib\netty-tcnative-boringssl-static-2.0.43.Final.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.68.Final.jar;%APP_HOME%\lib\netty-codec-http2-4.1.68.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.68.Final.jar;%APP_HOME%\lib\netty-resolver-dns-native-macos-4.1.68.Final-osx-x86_64.jar;%APP_HOME%\lib\netty-resolver-dns-4.1.68.Final.jar;%APP_HOME%\lib\netty-handler-4.1.68.Final.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.68.Final-linux-x86_64.jar;%APP_HOME%\lib\netty-transport-native-kqueue-4.1.68.Final-osx-x86_64.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.68.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.68.Final.jar;%APP_HOME%\lib\netty-codec-dns-4.1.68.Final.jar;%APP_HOME%\lib\netty-codec-4.1.68.Final.jar;%APP_HOME%\lib\netty-transport-4.1.68.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.68.Final.jar;%APP_HOME%\lib\jna-5.6.0.jar;%APP_HOME%\lib\accessors-smart-2.4.7.jar;%APP_HOME%\lib\jakarta.xml.bind-api-2.3.3.jar;%APP_HOME%\lib\jakarta.activation-api-1.2.2.jar;%APP_HOME%\lib\commons-jxpath-1.3.jar;%APP_HOME%\lib\antlr-runtime-3.4.jar;%APP_HOME%\lib\xmlpull-1.1.3.1.jar;%APP_HOME%\lib\reactive-streams-1.0.3.jar;%APP_HOME%\lib\netty-resolver-4.1.68.Final.jar;%APP_HOME%\lib\netty-common-4.1.68.Final.jar;%APP_HOME%\lib\nimbus-jose-jwt-9.9.3.jar;%APP_HOME%\lib\jcip-annotations-1.0-1.jar;%APP_HOME%\lib\content-type-2.1.jar;%APP_HOME%\lib\lang-tag-1.5.jar;%APP_HOME%\lib\asm-9.1.jar;%APP_HOME%\lib\woodstox-core-6.3.1.jar;%APP_HOME%\lib\stax2-api-4.2.1.jar;%APP_HOME%\lib\stringtemplate-3.2.1.jar;%APP_HOME%\lib\antlr-2.7.7.jar;%APP_HOME%\lib\servlet-api-2.5.jar


@rem Execute java-sdk-examples
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %JAVA_SDK_EXAMPLES_OPTS%  -classpath "%CLASSPATH%" io.orkes.samples.quickstart.WorkflowManagement %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable JAVA_SDK_EXAMPLES_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%JAVA_SDK_EXAMPLES_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
