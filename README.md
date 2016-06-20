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
Clone the repository to get the project. Run maven clean on the directory using the following command

    mvn clean verify

Set the workspace to the bundles/ directory in the cloned repository and add the project stored within it to your workspace and build it.

Right Click in Project Explorer > New > Java Project

When asked, uncheck the default location and set the location to bundles/org.apache.syncope.ide.eclipse.plugin

![Project Location](http://i.imgur.com/c2dXjBxl.png)

Add the target directory to the build path

![Target Directory Build Path](http://imgur.com/zivIsUSl.png)

Also add the PDE Plug-in dependencies library to the project

![Plug-in dependencies library](http://imgur.com/b4SevMnl.png)

## Running the plugin ##
Eclipse provides the functionality of running a separate instance of eclipse with the plugin installed in it. This provides us the opportunity to both test and debug our plugin in the eclipse environment away from our current instance of eclipse where the plugin is built.

Right Click on the project > Run As > Run Configurations > Add Eclipse Application > Run

![New Eclipse Application](http://i.imgur.com/yn0CQbgl.png)

This will start a new instance of Eclipse with our plugin installed. To view the plugin,

Go to Window > Show View > Other.. > Apache Syncope Category > Apache Syncope View

Click on Login and enter details for the syncope deployment

![Login Dialog](http://imgur.com/ONCaoWi.png)

Once you enter valid details and click OK on the dialog box, you will be able to see the Mail and Report Templates listed in the TreeView

![Plugin Tree View](http://imgur.com/spzrBBJ.png)
