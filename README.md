# artigo-bigtable
Apoio Artigo Bigtable

#GCP Big Table Java POC

###To run this POC you will need to create a Big Table Instance and connect to GCLOUD with your credentials. You need to have privileges BigTable Admin:

### Create BigTable Instance
``https://console.cloud.google.com/bigtable``

### 1 - Install GCLOUD: <br> 
``https://dl.google.com/dl/cloudsdk/channels/rapid/GoogleCloudSDKInstaller.exe``

### 2 - Login in: <br>
 ``gcloud auth application-default login``

 ----------------------------------
##Some commands to use with the client cbt

### Create BigTable Instance and table
``https://console.cloud.google.com/bigtable``


### Install interface cbt
``gcloud components install cbt``

### create table
``cbt createtable my-table``


### create file .cbtrc with the following content

``project = [project-id]``
<br>
``instance = [instance-id]``

### create a table:
``cbt createtable my-table``

### list tables
``cbt ls``

### Add one column family named cf1
``cbt createfamily my-table cf1``

### list column families
``cbt ls my-table``

### count register from a table
``cbt count bus-data``

### criar referencia para tabela do bigtable no bigqueryÇ
``bq mk --external_table_definition=my-bigtable-def teste.mytable2``


### setting version policy
``cbt setgcpolicy catalog descr maxversions=1``

###  - criar column family
``cbt createfamily catalog descr``


###  criar registro:
``pattern: cbt set <table> <rowID> <colFamily>:<colQualifier>=<value>``
``cbt set catalog sku123 descr:title="Antique Clock"``


### read table
``cbt read catalog``

### read last version of a record
``cbt read catalog cells-per-column=1``

### read one record
``cbt lookup catalog sku123``

### read a records range
``cbt read catalog start=sku124 end=sku942``

### read a records range using key parts
``cbt read catalog start=sku12 end=sku9``

### read a record using prefix
``cbt read catalog prefix=sku``

### read only 3 first rows
``cbt read catalog count=3``


### read ony 3 first rows (start with s, followed of 3 characters and the number 24)
``cbt read catalog regex=s.{3}24``


 ----------------------------------
##References:
https://medium.com/google-cloud/getting-started-with-bigtable-on-gcp-adfb896e0b2

https://cloud.google.com/sdk/gcloud

https://console.cloud.google.com/bigtable

https://cloud.google.com/bigtable

https://codelabs.developers.google.com/codelabs/cloud-bigtable-intro-java
