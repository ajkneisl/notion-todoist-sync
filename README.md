# Notion Todoist Sync

Sync your Todoist and your Notion. This works by detecting new tasks then appending it to a Notion block. Currently, it appends an embed of https://notion-widget-host.vercel.app/. 

## How to Use

- Download the JAR from the releases page.
- Run the JAR in the command line with `java -jar nts-{version}.jar` (with proper arguments.)

### Command Line Arguments

### Variables

- block id: To get the block ID, left-click an element on Notion and click "Copy link to block." Then, in the copied URL, everything past the # is the block ID.
- notion API key: You can get this by creating an integration on Notion, then copying the API key.
- todoist API key: You can find this by going on Todoist, Integrations, and it should be on the bottom of the page.

## Command Line Arguments

- `--notion:{notion API key}`: Define the Notion API key. (required)
- `--todoist:{todoist API key`: Define the Todoist API key. (required)
- `--useblock:{block id}`: Define the default Notion block to append tasks to.
- `--refresh:{time in ms}`: Define the time between each sync. (default is 5 minutes)
- `--project_specific:{true/false}`: Enable or disable the ability for tasks to be individually sorted through different project blocks.
- `--tie_project:{project name|block id}`: Define the block for a Todoist project. All new tasks for the project will be appended here.