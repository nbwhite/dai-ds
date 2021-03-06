What is DAI/DS?
====================

Thank you for your interest in DAI/DS!

DAI/DS (Data Access Interface and Data Store) is a data-centric framework supporting a suite of domain-specific adapters that organizes data gathered from components. The data store is a multi-tier "container" that hosts data defined by UCS (Unified Control System) and made available to components.

DAI/DS and UCS
====================

The Unified Control System (UCS) is intended to be part of a comprehensive software solution for the deployment and management of High Performance Computing (HPC) systems. The control system not only provides the system environment for the execution of applications and their workflows, but also provides the obvious point of control and service for administrators and operation staff who must configure, manage, track, tune, interpret and service the system to maximize availability of resource for the applications. 

UCS is a flexible software stack that assists development of new designs by OEMs and system vendors, often from new hardware targeted specifically for HPC. A unique feature of UCS, as compared to previous systems, is integration provided by a data-centric architecture. The control system gathers, organizes, and tracks system data to provide an accurate view of the system at all times. The data is organized by a Data Access Interface (DAI) component, and stored into the Data Store (DS) component. The DAI is an integrating component and is able to glean much of the data it organizes from its interaction with the other components.

https://unifiedcontrolsystem.github.io/


Travis CI &nbsp;&nbsp;&nbsp;&nbsp;[![Build Status](https://travis-ci.org/unifiedcontrolsystem/dai-ds.svg?branch=master)](https://travis-ci.org/unifiedcontrolsystem/dai-ds)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="https://scan.coverity.com/projects/unifiedcontrolsystem-dai-ds"><img alt="Coverity Scan Build Status" src="https://scan.coverity.com/projects/20195/badge.svg"/></a>
====================

The DAI/DS public GitHub project has been integrated with Travis continuous integration.

All pull requests will be built and tested automatically by Travis.


Building Instructions
=====================
DAI-DS is a Java 11 application with Python 3.6 command line tool(s). Both
methods require internet access to correctly build.

There are 2 ways to build DAI. The first is using docker and is by far the
most portable way. The second way is to use a host and install all the
prerequisites needed to do the build. Access to the internet is required in
both cases but proxies are allowed. In both build cases, the output will
appear in <repo_root>/build/ folder will be populated and the distribution
files location in <repo_root>/build/distributions/.

Docker Build
------------------------------------------------------------------------------
This is by far the most portable method. First install and configure docker.
The details depend on the specific OS distribution. The general
steps are:
1. Install and configure the docker package via the package manager on the
     OS (docker has documentation on this: https://docs.docker.com/).
   * Make sure the installed service is running and has internet access
   * Configure all docker users

2. Get and extract the DAI source tarball or another method to get the DAI
     source tree.
3. 'cd' to the extracted source tree's root.
4. In the source tree at the root do the following (Any proxy in
     'http_proxy' environment variable will be honored by the docker build
      script and execution):
    ```bash
    $ docker-build/docker-build.sh build
    ```
    or
    ```bash
    $ ./gradlew dockerBuild
    ```

This will build a docker image called 'dai_builder', then run the
container doing the ./gradlew build inside. The gradle persistent files
for the container will be stored at ~/.gradle/docker. This will save time
for subsequent builds.

Host Build
------------------------------------------------------------------------------
This is more complicated but is more useful for developers.
1. Make sure the host contains the following packages (actual names vary by
     OS distribution, the names below are for OpenSuse Leap 15.1):
    ```
    git
    rpm-build
    python3
    python3-devel
    python3-pip
    python3-setuptools
    java-11-openjdk
    java-11-openjdk-devel
    ```
__NOTE:__ Examine the docker-build/Dockerfile for how it's done for a docker
    container.

2. If a proxy is required for external access then please make sure of the
     following items:
    * Make sure gradle can access the internet by placing the updated lines
         in the ~/.gradle/gradle.properties file:
    ```
    systemProp.http.proxyHost=<proxy_host>
    systemProp.http.proxyPort=<proxy_port>
    systemProp.https.proxyHost=<proxy_host>
    systemProp.https.proxyPort=<proxy_port>
    systemProp.https.proxyUser=<proxy_user_if_needed>
    systemProp.https.proxyPassword=<proxy_password_if_needed>
    ```
    * Python 3's pip requires the proxy variable 'HTTPS_PROXY' is set to
        the proxy of the environment the host is inside.
3. Set the following properties in the ~/.gradle/gradle.properties file:
    ```
    systemProp.includeDebugSymbols=true
    ```
4. Get and extract the DAI source tarball or another method to get the DAI
  source tree.
5. 'cd' to the extracted source tree's root.
6. Build the deployable components.
    ```bash
    $ ./gradlew build
    ```


Run Requirements and Install Instructions (Deployment)
=======================================================

Pre-requisites:
----------------
<ol>
<li>System must have at least 32GB of RAM and 8 cores to run smoothly.</li>
<li>Java 11 JDK</li>
<li>The "curl" program must be installed on DAI-DS hosts.</li>
<li>Both docker and docker-compose must be installed and configured for the root user if using 3rd party docker components.</li>
<li>ALL configuration files changed to support the target system. This includes:</li>
<ul>
<li>SystemManifest.json</li>
<li>MachineConfig.json</li>
<li>NearlineConfig.json</li>
<li>ProviderMonitoringNetworkForeignBus.json</li>
<li>ProviderProvisionerNetworkForeignBus.json</li>
<li>LocationTranslationMap.json - <i><b>If the contents of this file do not correlate with
                                    the MachineConfig.json file's contents, the
                                    provisioning and monitoring providers may not
                                    function as expected. Make sure ALL Foreign
                                    names match a DAI name and no "location" is missing
                                    or unexpected.</b></i></li>
</ul>                                    
<li>The real target API (or EventSim container) must be available for the
   ProviderMonitoringNetworkForeignBus and ProviderProvisionerNetworkForeignBus
   to connect.</li>
<li>If the deployment system for the first use case below does not have internet access you must download and save the following docker images from Docker Hub then copy and reload them onto the target system's local docker repository:</li>
<ul>
<li>postgres:11.4</li>
<li>rabbitmq:3.7-management</li>
<li>voltdb/voltdb-community:9.2.1</li>
<li>openjdk:11.0.5-jdk</li>
</ul>
<li>Python 3.5 or 3.6 plus the following PIP packages (for hosts installing cli or eventsim-cli):</li>
<ul>
<li>requests</li>
<li>clustershell</li>
<li>python-dateutil</li>
<li>progress</li>
<li>texttable</li>
<li>timeout-decorator</li>
</ul>
</ol>

Packages (.rpm files are shown but there are .deb equivelents if enabled in the build<sup>$</sup>)
---------------------------------------------------------------------------------------------------
<table BORDER=2>
<tr style="background-color: navy; color: yellow; font-style: italic"><th>Package Name</th><th>Required</th><th>Description</th></tr>
<tr><td>dai-{version}.noarch.rpm</td><td>Yes</td><td>This package contains the istallation of the DAI-DS application. A configuration RPM must be installed for the DAI-DS to function correctly.</td></tr>
<tr><td>dai-3rd-party-{version}.noarch.rpm</td><td>No<sup>*</sup></td><td>This package contains the docker images and docker-compoose YAML files that are used for testing or quick trials. This includes VoltDB, Postgres DB, and RabbitMQ containers. Starting these contains is turnkey and they will all be ready for use.</td></tr>
<tr><td>dai-cli-{version}.noarch.rpm            </td><td>Yes</td><td>This package contains the UCS CLI program minus it's configuration. A configuration RPM must be installed.</td></tr>
<tr><td>dai-hw1-config-{version}.noarch.rpm     </td><td>No<sup>**</sup></td><td>This package contains the DAI-DS configuration for hardware configuration #1. Only one configuration can be installed on the DAI-DS host at a time!</td></tr>
<tr><td>dai-hw2-config-{version}.noarch.rpm     </td><td>No<sup>**</sup></td><td>This package contains the DAI-DS configuration for hardware configuration #2. Only one configuration can be installed on the DAI-DS host at a time!</td></tr>
<tr><td>dai-hw3-config-{version}.noarch.rpm     </td><td>No<sup>**</sup></td><td>This package contains the DAI-DS configuration for hardware configuration #3. Only one configuration can be installed on the DAI-DS host at a time!</td></tr>
<tr><td>dai-postgres-schema-{version}.noarch.rpm</td><td>Yes<sup>***</sup></td><td>This package contains stuff to populate the postgres schema. It must be installed on the same server with the postgres database install. This need not be the same location as VoltDB or DAI.</td></tr>
<tr><td>dai-volt-schema-{version}.noarch.rpm    </td><td>Yes<sup>***</sup></td><td>This package contains the schema files and loadable JAR files for VoltDB setup. This does not contain the <span style="font-family: monospace; text-decoration: underline">loadVoltDbInitialData.sh</span> script. This is intended to be loaded on the VoltDB server host where the sqlcmd is present after VoltDB install is completed. The <span style="font-family: monospace; text-decoration: underline">loadVoltDbInitialData.sh</span> script can be run from the DAI-DS installed host remotely to populate the schema with initial data.</td></tr>
<tr style="background-color: navy; color: yellow; font-style: italic"><th>EventSim Related</th><th></th><th></th></tr>
<tr><td>dai-eventsim-server-{version}.noarch.rpm</td><td>No<sup>**</sup></td><td>This package contains the EventSim REST server that takes rquests from the EventSim CLI and simulates the target systems behavior wrt telemetry, events, inventory, etc...</td></tr>
<tr><td>dai-eventsim-cli-{version}.noarch.rpm   </td><td>No<sup>*</sup></td><td>This package contains the EventSim CLI for simulating scenarios during simulation. The EventSim configuration RPM must be installed for EventSim to function correctly.</td></tr>
<tr><td>dai-eventsim-config-{version}.noarch.rpm</td><td>No<sup>*</sup></td><td>This package contains the DAI-DS configuration for EventSim using a small machine configuration. Only one configuration can be installed on the DAI-DS host at a time!</td></tr>
</table>
<ul style="list-style-type:none">
<li><span style="font-family: monospace"><sup>$</sup>&nbsp;&nbsp;&nbsp;</span>To enable Debian compatible packages add "systemProp.includeDebianPackages=true" to your ~/.gradle/gradle.properties file.</li>
<li><span style="font-family: monospace"><sup>*</sup>&nbsp;&nbsp;&nbsp;</span>When testing with EventSim or using EventSim for evaluation these must be installed. On a real system voltDB, Postgres, and RabbitMQ servers must manually be installed.</li>
<li><span style="font-family: monospace"><sup>**</sup>&nbsp;&nbsp;</span>When using Eventsim for trial or testing the EventSim server, EventSim CLI, and EventSim configuration should be installed.</li>
<li><span style="font-family: monospace"><sup>***</sup>&nbsp;</span>These are not installed on DAI-DS systems unless DAI-DS is also on a host with the Postgres or VoltDB databases.</li>
</ul>

Use Cases for Installation
===========================
Installation of all DAI-DS packages are in the <b>/opt/ucs/**</b> tree.

__NOTE:__ This single host using eventsim scenario's below run on a single system out-of-the box if you edit the
___/etc/hosts___ file and add "_am01-nmn.local_" and "_am01-nmn_" as an aliases
for the system. Then stop using the services (see below). If you are behind a proxy
please make sure that the no_proxy and NO_PROXY environmental variables have the
"_.local_" domain excluded from the proxy lookups. The non-domain alias name "_am01-nmn_"
is required only for the voltdb container to start correctly.

Testing and Evaluation on One Host (Docker and Eventsim)
---------------------------------------------------------
<dl>
<dt style="font-style: italic">Scenario:</dt>
<dd>As stated in the title this is a single system for testing or evaluation using docker for third party copmponents.</dd>
<dt style="font-style: italic">Install DAI-DS Packages:</dt>
<dd><ul style="list-style: none">
<li>dai-3rd-party-{version}.noarch.rpm</li>
<li>dai-{version}.noarch.rpm</li>
<li>dai-cli-{version}.noarch.rpm</li>
<li>dai-eventsim-server-{version}.noarch.rpm</li>
<li>dai-eventsim-cli-{version}.noarch.rpm</li>
<li>dai-eventsim-config-{version}.noarch.rpm</li>
</ul></dd>
<dt style="font-style: italic">Third Party Component Setup:</dt>
<dd><ol>
<li># systemctl start dai-postgres.service</li>
<li># systemctl start dai-rabbitmq.service</li>
<li># systemctl start dai-voltdb.service</li>
<li># systemctl start dai-manager.service</li>
<li># systemctl start dai-eventsim.service</li>
</ol></dd>
</dl>

Testing and Evaluation on One Host (Non-Docker and Eventsim)
-------------------------------------------------------------
<dl>
<dt style="font-style: italic">Scenario:</dt>
<dd>As stated in the title this is a single system for testing or evaluation without using docker.</dd>
<dt style="font-style: italic">Manually Install on your Host:</dt>
<dd><ul style="list-style: none">
<li>VoltDB 9.2.*</li>
<li>PostgreSQL 11.4.*</li>
<li>RabittMQ 3.7.*</li>
</ul></dd>
<dt style="font-style: italic">Install DAI-DS Packages:</dt>
<dd><ul style="list-style: none">
<li>dai-postgres-schema-{version}.noarch.rpm</li>
<li>dai-volt-schema-{version}.noarch.rpm</li>
<li>dai-{version}.noarch.rpm</li>
<li>dai-cli-{version}.noarch.rpm</li>
<li>dai-eventsim-server-{version}.noarch.rpm</li>
<li>dai-eventsim-cli-{version}.noarch.rpm</li>
<li>dai-eventsim-config-{version}.noarch.rpm</li>
</ul></dd>
<dt style="font-style: italic">Third Party Component Setup:</dt>
<dd><ol>
<li>Configure Postgres with users and "dai" database and restart postgres service on your host</li>
<li>Configure and start RabbitMQ on your host</li>
<li>Configure and start VoltDB on your host (make sure both locahhost and management IPs on this host are active)</li>
<li>Populate VoltDB DB by running: <span style="font-family: monospace; font-weight: bold">/opt/ucs/share/for_voltdb/setupVoltDbSchema.sh</span></li>
<li>Populate Postgres DB schema by running: <span style="font-family: monospace; font-weight: bold">/opt/ucs/share/for_postgres/setupInitialPostgresSchema.sh</span></li>
</ol></dd>
<dt style="font-style: italic">Start DAI-DS:</dt>
<dd><ol>
<li># systemctl start dai-manager.service</li>
</ol></dd>
</dl>

Fully Separate Installs (Multi-host)
-------------------------------------
This procedure assumes you have a configuration for you machine layout already defined and called <b>my-config.noarch.rpm</b>.
<dl>
<dt style="font-style: italic">Scenario:</dt>
<dd>This scenerio assumes that you have a separate host for each role in the DAI-DS system. We will define a VoltDB server host (voltdb), a RabbitMQ server host (rabbitmq), a Postgres DB server host (postgres), 2 DAI-DS software hosts (dai1 and dai2), and an administrator access node (admin). </dd>
<dt style="font-style: italic">Install and Setup <b>postgres</b> Host:</dt>
<dd><ol>
<li>Install PostgreSQL 11.4.* on this host</li>
<li>Configure Postgres with users and "dai" database and restart postgres service on this host</li>
<li>Install <b>dai-postgres-schema-{version}.noarch.rpm</b></li>
<li>Populate Postgres DB's schema by running: <span style="font-family: monospace; font-weight: bold">/opt/ucs/share/for_postgres/setupInitialPostgresSchema.sh</</ol></dd>
<dt style="font-style: italic">Install and Setup <b>voltdb</b> Host:</dt>
<dd><ol>
<li>Install VoltDB 9.2.* on this host</li>
<li>Install <b>dai-volt-schema-{version}.noarch.rpm</b> on this host</li>
<li>Install <b>my-config.noarch.rpm</b> on this host</li>
<li>Configure and start VoltDB on this host (make sure both locahhost and management IPs on this host are active)</li>
<li>Populate VoltDB DB by running: <span style="font-family: monospace; font-weight: bold">/opt/ucs/share/for_voltdb/setupVoltDbSchema.sh</span></li>
</ol></dd>
<dt style="font-style: italic">Install <b>rabbitmq</b> Host:</dt>
<dd><ol>
<li>Install RabittMQ 3.7.*</li>
<li>Configure for remote access and start RabbitMQ on your host</li>
</ol></dd>
<dt style="font-style: italic">Install <b>da1</b> and <b>dai2</b> Hosts:</dt>
<dd><ol>
<li>Install <b>dai-{version}.noarch.rpm</b> on hosts dai1 and dai2</li>
<li>Install <b>my-config.noarch.rpm</b> on hosts dai1 and dai2</li>
</ol></dd>
<dt style="font-style: italic">Install <b>admin</b> Host:</dt>
<dd><ol>
<li>Install <b>dai-cli-{version}.noarch.rpm</b> on this host</li>
<li>Install <b>my-config.noarch.rpm</b> on this host</li>
</ol></dd>
<dt style="font-style: italic">Start DAI-DS on dai1 and dai2:</dt>
<dd><ol>
<li># systemctl start dai-manager.service</li>
</ol></dd>
</dl>

Checking for Running DAI Components:
-------------------------------------
If your host system has the Java JDK 8 or newer installed (JRE alone is not enough) then there is an included
script called ___show_adapters___ which will show a detailed list of running DAI-DS java processes. This tool
must be run as the root user.

If you don't have a new Java JDK installed, and are using the services then use the normal systemctl tool to
check the status of the services.

Log Output:
------------
Log output in either execution case will be in:
<ul>
<li><b>/opt/ucs/log/*</b> for EventSim and DAI components log files.</li>
<li><b>/opt/ucs/docker/tier1/log/*</b> for voltdb and the schema population containers log files.</li>
<li>Postgres logs are not available at this time.</li>
</ul>

Uninstalling DAI and Third Party Components:
--------------------------------------------
Since all DAI-DS components are RPMs (or debian packages) use the normal method of uninstallation for your distribution. Please not that all services will be stopped gracefully during uninstallation. Log files will remain in the /opt/ucs.** tree.

Notes When Using Docker Containers:
------------------------------------
* Postgres persistent data is stored in /opt/ucs/docker/tier2/data/pgdata
    + To clear the data and start over do the following as root:
        ```bash
        # systemctl stop dai-manager dai-voltdb dai-rabbitmq dai-postgres
        # rm -rf /opt/ucs/docker/tier2/data/pgdata
        ```
    + To backup the data (as root):
        ```bash
        # systemctl stop dai-manager dai-voltdb dai-rabbitmq dai-postgres
        # recursively copy/tar /opt/ucs/docker/tier2/data/pgdata
        ```
* If you start all services together with one command, expect errors and
  restarts of the dai-manager service until all other services are running and setup.

<!--
<hr />
<hr />
<hr />
<h1>OLD Install Stuff Below</h1>

Installers to use after build:
```
build/distributions/install-docker_3rd_party_{version}.sh
build/distributions/install-docker_dai_{version}.sh
```

These install packages include 4 services to control docker-compose
containers. One each for postgres, voltdb, rabbitmq, and dai. It assumes all
adapters run on one system. The EventSim application must be run as a
standalone docker-compose application using the eventsim.yml file.

__NOTE:__ This will run on a single system out-of-the box if you edit the
___/etc/hosts___ file and add "_sms01-nmn.local_" and "_sms01-nmn_" as an aliases
for the system. Then stop using the services (see below). If you are behind a proxy
please make sure that the no_proxy and NO_PROXY environmental variables have the
"_.local_" domain excluded from the proxy lookups. The non-domain alias name "_sms01-nmn_"
is required only for the voltdb container to start correctly.

Pre-requisites:
----------------
1. System must have at least 32GB of RAM to run smoothly.
2. docker installed and configured for the root user.
3. docker-compose installed.
4. ALL configuration files changed to support the target system. This includes:
    * SystemManifest.json
    * MachineConfig.json
    * NearlineConfig.json
    * ProviderMonitoringNetworkForeignBus.json
    * ProviderProvisionerNetworkForeignBus.json
    * LocationTranslationMap.json - If the contents of this file do not correlate with
                                    the MachineConfig.json file's contents, the
                                    provisioning and monitoring providers may not
                                    function as expected. Make sure ALL Foreign
                                    names match a DAI name and no "location" is missing
                                    or unexpected.
5. The real API (or EventSim container) must be available for the
   ProviderMonitoringNetworkForeignBus and ProviderProvisionerNetworkForeignBus
   to connect.
6. If the deployment system does not have internet access you must download and save the following docker images from Docker Hub then copy and reload them onto the target system's local docker repository:
    * postgres:11.4
    * rabbitmq:3.7-management
    * voltdb/voltdb-community:9.2.1
    * openjdk:11.0.5-jdk


Installing DAI and Third Party Components:
-------------------------------------------
1. As root run the third party installer:
    ```bash
    # install-docker_3rd_party_{version}.sh
    ```
2. As root run the dai installer:
    ```bash
    # install-docker_dai_{version}.sh
    ```
__NOTE:__ The installed file tree is located at `/opt/dai-docker`.

__NOTE:__ After installation of the DAI components all systemd services are enabled
but not started automatically.

Starting/Stopping Services:
----------------------------
1. sudo systemctl start dai-postgres
2. sudo systemctl start dai-rabbitmq
3. sudo systemctl start dai-voltdb
4. sudo systemctl start dai-manager

__NOTE:__ If you start only the dai-manager service then all other services will
      start as dependencies if not already running but may experience timing issues and not start up properly.

These services all use the /opt/dai-docker/*.yml files for docker-compose
launching.  _Do not use docker or docker-compose directly, without first stopping the services and disabling them_. Attempting to stop a container directly will just cause it to restart as the service is set to restart the container on container exit.

Stopping or restarting is done as either services only or docker-compose only. _Do not mix these control techniques_.

__NOTE:__ If you are using EventSim instead of a real API you will need stop and disable the services and use docker-compose directly. Then start the containers in the following order as root:

1. cd /opt/dai-docker
2. docker-compose -f postgres.yml up -d
3. docker-compose -f rabbitmq.yml up -d
4. docker-compose -f voltdb.yml up -d _(wait 40 seconds for this to completely stablize)_
5. docker-compose -f eventsim.yml up -d
6. docker-compose -f dai.yml up -d

Checking for Running DAI Components:
-------------------------------------
If your host system has the Java JDK 8 or newer installed (JRE alone is not enough) then there is an included
script called ___show_adapters___ which will show a detailed list of running DAI-DS java processes. This tool
must be run as the root user.

If you don't have a new Java JDK installed, and are using the services then use the normal systemctl tool to
check the status of the services.

If you are using docker-compose instead of the services then the following line will show the DAI-DS running
containers:
```bash
# docker ps -a | grep "dai-"
```

Log Output:
------------
Log output in either execution case will be in:
* /opt/dai-docker/log/* for EventSim and DAI components.
* /opt/dai/docker/tier1/log/* for voltdb and the schema population containers logs
* Postgres logs are not available at this time.

Uninstalling DAI and Third Party Components:
--------------------------------------------
Note: The following commands will stop the services if they are running.

1. As root run the installer with "-U" to uninstall DAI.
    ```bash
    # install-docker_deploy_dai_{version}.sh -U
    ```
2. As root run the installer with "-U" to uninstall third party components.
    ```bash
    # install-docker_deploy_3rd_party_{version}.sh -U
    ```


Notes:
-------
* Postgres persistent data is stored in /opt/dai-docker/tier2/data/pgdata
    + To clear the data and start over do the following as root:
        ```bash
        # systemctl stop dai-manager dai-voltdb dai-rabbitmq dai-postgres
        # rm -rf /opt/dai-docker/tier2/data/pgdata
        ```
    + To backup the data (as root):
        ```bash
        # systemctl stop dai-manager dai-voltdb dai-rabbitmq dai-postgres
        # recursively copy/tar /opt/dai-docker/tier2/data/pgdata
        ```
* If you start all services together with one command, expect errors and
  restarts of the dai-manager service until all other services are running.
-->
