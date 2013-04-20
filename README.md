# MenuMod

MenuMod is a simple application for editing restaurant menus. This is a sample
Play framework application that demonstrates the Play forms API MongoDB storage
and persistence without user accounts.

![screen shot](https://raw.github.com/hilton/menumod/master/screenshot.png)

## Forms

The user-interface uses JQuery to let you add a hierarchy of sections and dishes
to the menu. The controller form mappings expect a list of sections, and a list
of dishes for each section.

## Data access

The MongoDB data access layer uses Play 2.1 JSON to map between the model and
MongoDB. The intention is to use the simplest possible access to MongoDB, which
is why Salat is not used.

## No user accounts

Instead of using user accounts to access stored menus, each menu is identified
by a UUID in a URL that you can bookmark. The last-viewed ‘current’ menu’s UUID
is saved in a cookie so you can navigate to the current menu.

## Interesting enhancements

**Read-only view:** add a ‘slug’ (e.g. `memory-lane`) for each menu, and add a read-
only view with a URL based on the slug, like ‘/memory-lane’. Then add a public
list of all menus to the front-page.

**CSS:** save custom CSS for each menu. Allow users to choose another menu’s CSS
instead of writing their own.

**Full-text search:** search menus for your favourite dishes.

**Versioning:** generate a new UUID every time you click ‘Save Changes’ and save a
whole new document in MongoDB. Include a history of previous versions’ UUIDs
with each menu and display a version history.

**Localisation:** support multiple languages for a menu. Suggest existing
translations used for dishes with similar names.
