# MenuMod

MenuMod is a simple application for editing restaurant menus. This is a sample Play framework application that
demonstrates MongoDB storage and persistence without user accounts.

![screen shot](https://raw.github.com/hilton/menumod/master/screenshot.png)

## Data access

The MongoDB data access layer uses Play 2.1 JSON to map between the model and MongoDB. The intention is to use the simplest possible access to MongoDB, which is why Salat is not used.

## No user accounts

Instead of using user accounts to access stored menus, each menu is identified by a UUID in a URL that you can bookmark.
The last-viewed ‘current’ menu’s UUID is saved in a cookie so you can navigate to the current menu.
