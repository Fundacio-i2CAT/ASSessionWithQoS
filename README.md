![i2cat](https://ametic.es/sites/default/files//i2cat_w.png)

# Short description
Java implementation of the ASSessionWithQoS API for Open5Gs.

# Pre-requisites
**Build tools**
* Java 17
* Maven 3

**Deployment tools**
* Docker

**External dependencies**
* Open5Gs >= 2.7.0 (might work with older versions but it has not been tested)
* L3 connectivity to Open5Gs' PCF and MongoDB
* CAMARA QoD Transformation Function by Deutsche Telekom (tested with version 0.8.0)

# Build
Build the JAR file as follows
```bash
cd api
mvn clean package
```
You can then build a Docker image:
```bash
docker build . -t assessionwithqos
```

# Technical description
REST API built with Spring Boot that implements the ASSessionWithQoS API. It is meant to be used in conjunction with the CAMARA's QoD Transformation Function. For details on how to deploy the whole system, [see here](docs/deployment.md). You can also check some [examples](docs/examples.md) on how to call the QoD TF API.

# Future Work
* Upgrade to latest version of the QoD TF. 
* Validate QoD TF with Redis DB instead of in-memory H2.
* Add new endpoint(s) to allow QoS profiles to be defined dynamically.


# Source
This code has been developed within the research / innovation projects UNICO 6GSMART, SNS 6G-XR and SNS SUNRISE-6G.

# Copyright
This code has been developed by Fundació Privada Internet i Innovació Digital a Catalunya (i2CAT).
i2CAT is a *non-profit research and innovation centre* that  promotes mission-driven knowledge to solve business challenges, co-create solutions with a transformative impact, empower citizens through open and participative digital social innovation with territorial capillarity, and promote pioneering and strategic initiatives.
i2CAT *aims to transfer* research project results to private companies in order to create social and economic impact via the out-licensing of intellectual property and the creation of spin-offs.
Find more information of i2CAT projects and IP rights at https://i2cat.net/tech-transfer/

# License
This code is licensed under the terms *Affero GPL v3*. Information about the license can be located at https://www.gnu.org/licenses/agpl-3.0.en.html.

If you find that this license doesn't fit with your requirements regarding the use, distribution or redistribution of our code for your specific work, please, don’t hesitate to contact the intellectual property managers in i2CAT at the following address: techtransfer@i2cat.net