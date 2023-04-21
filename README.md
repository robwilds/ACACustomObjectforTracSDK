# salesdemo-alfresco-sdk

#get started

STEP 1

opensdk (not JDk): https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_osx-x64_bin.tar.gz

extract the archive and move the resulting folder to:  /Library/Java/JavaVirtualMachines

then run the following command from terminal:


$ env|grep JAVA_HOME
 JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.1.jdk/Contents/Home

STEP 2
 Now install homebrew:  /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

 STEP 3
 once homebrew is installed, get maven (from terminal): brew install maven


 STEP 4
 download intelliJ community edition and isntall: https://www.jetbrains.com/idea/download/download-thanks.html?platform=mac&code=IIC

STEP 5
once everything is installed, open the existing project in intelliJ


<img width="691" alt="image" src="https://user-images.githubusercontent.com/37511730/194180857-1f1767b1-d47f-4438-abaf-1b852f809bca.png">

STEP 6

Once project is open, navigate to:  salesdemo>src>main>resources>model.  you will see a content-model.xml file.  this file must be edited to show custom properties and labels in ACA

STEP 7

After editing the content-model.xml file, oepn terminal within intelliJ
<img width="460" alt="image" src="https://user-images.githubusercontent.com/37511730/194322714-0a7c4f1a-29c9-4c8a-a1d5-9499f8f74ae1.png">

after opening terminal, navigate into the salesdemo sub folder and compile the jar with the following commands:  mvn clean package.  you should see build success.

STEP 8

now upload the jar to your ACS instance.  the jar is in the salesdemo/target folder.  
![image](https://user-images.githubusercontent.com/37511730/194324255-7b759e8f-2c71-41ba-9722-d33211ba2ae8.png)

the folder to upload is in the:  data/services/content/custom folder
<img width="363" alt="image" src="https://user-images.githubusercontent.com/37511730/194324977-ce7384c0-84ae-45c7-ad72-2518a3102412.png">

example transfer command:  scp -i yourkey.pem salesdemo-1.0-SNAPSHOT.jar ec2-user@ec2-3-91-158-74.compute-1.amazonaws.com:/home/ec2-user/adp/data/services/content/custom/


restart services then login to ACA (as demo or admin) and go to admin>object types and you should see the two types listed in the add types dropdown

create your trac!





