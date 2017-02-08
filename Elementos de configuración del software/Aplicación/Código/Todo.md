## TODO

#### Priority:
* **(Dropped)**: Add Manager user implementation
* **(Dropped)**: Add Controller hooks to panels
* **(Dropped)**: Finish ModifyOrder flow
* **(Dropped)**: Make manager stock table autosorting by clicking on header
* **(Dropped)**: Remove Clasificar Inventario button in ^
* **(Low)**: Change `OrderDao#searchOrder` and `SaleDao#searchSale` implementation
* **(Dropped)**: Update the vendor table in real time (as soon as I select a product)
* **(Dropped)**: Add a pop-up window for Order confirmation in the Administration panel
* ~~Test Administration panel~~

#### Not a priority:
* Make searchUser search string queries in Permissions
* Add field to Sales (and Orders) to tell who finished the Sale/Order
* Figure a way to notify Admin and StockPanel of Order details without overriding the Stock details (both use the CONTENT event)
* **(Low)**: Figure out how to deal with SQLExceptions
* **(Low)**: Remove redundant TitledBorders on the pop-ups
* **(Low)**: Show hints again after changing panels on Login / Signup
