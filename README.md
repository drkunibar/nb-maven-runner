# Netbeans Maven Runner

This plugin is inspired by the IntelliJ IDEA Maven side panel.

![Maven Runner Panel](doc/maven-runner.png)

## The Toolbar

![Maven Runner Toolbar](doc/maven-runner-toolbar.png)

The toolbar shows following buttons:

* **Refresh Tree:** Refresh the Maven-Runner tree
* **Run Project:** Runs the project currently selected in the Maven Runner
* **Debug Project:** Debugs the project currently selected in the Maven Runner
* **Build Project:** Builds the project currently selected in the Maven Runner
* **Clean and Build Project:** Rebuild the project currently selected in the Maven Runner

* **Expand all:** Expands all nodes in the Maven Runner
* **Collapse all:** Collapse all nodes in the Maven Runner

## Favorites

You add actions, goals etc. to the 'Favorites' node.

![Maven Runner Favorites](doc/maven.runner-favorites.png)

## Profiles

This node contains all declared profiles (pom.xml, settings.xml and configurations). 
The currently active profiles are marked with a selected checkbox.

![Maven Runner Profiles](doc/maven-runner-profiles.png)

## Actions

The *Actions* node contains the IDEs default actions *Run*, *Debug*, *Build*, 
*Clean and Build*, all the actions you have declared in the project and the 
action have defined in the Netbeans Maven options. You can run any action by 
double click.

![Maven Runner Actions](doc/maven-runner-actions.png)

Like the *Lifecycle*, the declared actions have a context menu, where you can 
change the behavior of the execution.

![Maven Runner Action Menu](doc/maven-runner-lifecycle-menu.png)

If you use the `Execute Goal With Modifiers...` you get the same dialog as in 
the Maven project context menu.

![Maven Runner Goal Modifier](doc/maven-runner-goal.png)

You find this behavior on all *Goals* (Actions, Lifecycle, Plugins).

## Lifecycle

The *Lifecycle* node contains all phases of the Maven lifecycle to which a goal 
is bound. You can see the bound goals in the tool tip.

![Maven Runner Lifecycle Tooltip](doc/maven-runner-lifecycle.png)

## Plugins

This node contains the plugins and their goals. Each goal have a context menu 
entry `Show Documentation...` 

![Maven Runner Show Documentation](doc/maven-runner-goal-help.png)

which shows the documentation of the plugin goal in the *Output* panel.

![Maven Runner Documentation Output](doc/maven-runner-goal-help-output.png)

# Changelog

## 1.2.0
- Fix 'restore' view
- Add feature to use 'favorites'

## 1.1.0
- 'Real' project icons
- Update external libs

## 1.0.0

- First Version