# SYNCOPE-809-EclipsePlugin
Syncope-809 GSoC Project (Eclipse Plugin for Apache Syncope deployment : Allows user to view and edit Mail Templates and Report Templates from Eclipse itself)

## Building ##
This project includes an Eclipse Project that works only in a Plugin Development Environment actiated eclipse installation. This means that if you already have a previous version of eclipse or one that does not include PDE, you will have to install it. 

### How to install PDE ###
Go to Help > Eclipse Marketplace

Search for "pde" in the marketplace

Install the Eclipse PDE (Plug-in Development Environment) 3.10 Luna
(Works on Eclipse Mars2)
![PDE Installation](http://imgur.com/d7VSJre.png)
### Setup project in workspace ###
Clone the repository to get the project. Then import the project to your desired workspace.

Right Click in Project Explorer > Import > General > Existing Projects into Workspace

### Update and copy dependencies into project ###
Assuming you have maven installed* and are able to access mvn command using the terminal/cmd, open a terminal/cmd window inside the EclipsePlugin project folder and run the following command.

    mvn dependency:copy-dependencies -DoutputDirectory=${project.build.directory}/lib
    
This will download and copy the dependencies to the projects target/lib folder. This is necessary since it provides the libraries required to connect to the syncope deployment.

## Running the plugin ##
Eclipse provides the functionality of running a separate instance of eclipse with the plugin installed in it. This provides us the opportunity to both test and debug our plugin in the eclipse environment away from our current instance of eclipse where the plugin is built.

Right Click on EclipsePlugin project > Run As > Eclipse Application

This will start a new instance of Eclipse with our plugin installed. To view the plugin,

Go to Window > Show View > Other.. > Syncope Category > Syncope View

Click on Login and enter details for the syncope deployment

![Login Dialog](http://imgur.com/ONCaoWi.png)

Once you enter valid details and click OK on the dialog box, you will be able to see the Mail and Report Templates listed in the TreeView

![Plugin Tree View](http://imgur.com/spzrBBJ.png)
