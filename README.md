# PriceComparingMobileApp
## Table of contents
* [Description](#description)
* [Technologies](#technologies)
* [Roadmap](#roadmap)
* [Roles](#roles)
* [As a User, you can](#as-a-user-you-can)
* [Instruction](#instruction)
* [Diagrams](#diagrams)
* [Future plans](#future-plans)

## Description
This project is a mobile application for comparing prices on different smartphones (in the future other categories will be added) which can be run on Android devices. The project was developed as the coursework of Software Architecture.

## Technologies
* Kotlin
* XML
* SQLite
* Volley (Kotlin library for HTTP requests)

## Roadmap
* create a connection with API and parse incoming JSONs
* create database and fill it the data got from API
* create convenient GUI
* add caching with data update if more than an hour has come since the last data update
* add filtration
* add sorting

## Roles
* User

## As a User, you can:
* read the instruction on the main page
* open product list by choosing necessary category in the side menu
* use sorting by price
* use filtration by price
* use search by product name
* open product page
* view specifications of the product
* move to "PRICE COMPARING" tab and view list of price suggestions
* sort price suggestions by alphabet A to Z or Z to A and by price low to high or to high to low
* follw the link to visit product's page on the shop's website

## Instruction
1. Afer opening the application you can see the brief instruction in the main activity.

![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/screenshots/main_activity.png?raw=true)

*Main activity*

2. You should open side menu and choose category and subcategory (now only "Mobiles" is available).

![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/screenshots/main_activity_with_side_menu.png?raw=true)

*main activity with opened side menu*

3. After choosing the right subcategory (the list of products will be available) it is possible to use search near dandruff icon.

![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/screenshots/list_of_products.png?raw=true)

*List of Products*

4. You can also sort products by price low to high or high to low after opening side menu. After choosing the right sorting option you should press the button "SHOW".

![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/screenshots/sorting_options.png?raw=true)

*Choosing sorting option in the side menu*

![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/screenshots/sorting_result.png?raw=true)

*Sorting result*

5. Then you canfilter products py minimal and/or maximal price in the side menu. After entering necessary minimal and/or maximal price values you should press the button "SHOW".

![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/screenshots/filtering_options.png?raw=true)

*Entering price limits for filtration in the side menu*

![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/screenshots/filtering_result.png?raw=true)

*Fitration result*

6. After finding proper varient you can open open it by tapping on the product.
7. In the product page you can move between two tabs: "Specifications" and "Price Comparing".

![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/screenshots/product_page.png?raw=true)

*Product's specifications on the product's page*

8. In "Specifications" you can view product's specifications.
9. In "Price Comparing" you can view all the shops' suggestions.

![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/screenshots/shops_suggestions.png?raw=true)

*Product's page with shops' suggestions*

10. You can sort shops' suggestions by alphabet A to Z or Z to A and by price low to high or to high to low by clicking appropriate option.

![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/screenshots/shops_suggestions_after_change.png?raw=true)

*Shops' suggestions after choosing Z to A filtering option*

12. You can visit product's page on the shop's website by tapping the link in appropriate suggestion.

## Diagrams
### Use case diagram
![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/diagrams/use_case_diagram.png?raw=true)
### Business processes diagrams
#### Finding necessary product business process
![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/diagrams/use_case_diagram.png?raw=true)
#### Choosing the right shop's suggestion business process
![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/diagrams/use_case_diagram.png?raw=true)
### ER diagram
![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/diagrams/use_case_diagram.png?raw=true)
### Architectural diagram
![alt text](https://github.com/MykolaOnyshchuk/PriceComparingMobileApp/blob/master/diagrams/use_case_diagram.png?raw=true)

## Future plans
* add more categories
* add more filtration and sorting options
* improve UI/UX
