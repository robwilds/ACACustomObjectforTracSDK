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

After editing the content-model.xml file, compile the jar with the following commands:  mvn clean package from a terminal opened within intelliJ



