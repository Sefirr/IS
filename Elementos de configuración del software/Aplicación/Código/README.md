# Fast and Food v0.6 #

### Uso
- Tener un servidor con mysql instalado. 
- Crear una base de datos con la estructura [indicada](https://www.dropbox.com/s/bmfjr4a4e8xpxh1/ff.sql.gz?dl=0).
- Si no existe, crear un archivo `database.properties` en el mismo directorio que el .jar
- Modificar las propiedades con los datos apropiados.
- Ejecutar el .jar con `$ java -jar /path/to/jar`


### Branches

* #### Estables[^1]:  
    - **master**: `Stable` Contiene los últimos cambios al programa.
    - **release**: Representa una versión "completa" y se considera apta para su uso. 
    
## 

* #### No estables[^2]:  
    - **dev/\***: `Unstable` Representa una línea de desarrollo presente. Merge en master cuando alcanza un estado estable.        
    - **old/\***: Contiene líneas de desarrollo abandonadas.


[^1]: Una rama `Stable` se considera aquella sin errores de compilación y sin bugs severos.  
Una rama `Release` es aquella que es `Stable`, y que además implementa las suficientes funcionalidades como para considerarse apta para su presentación.

[^2]:Una rama `Unstable` se considera aquella en la que se está trabajando, y por tanto puede no compilar o tener bugs severos.  

### Bug Fixing ###
- Flow and Class diagrams don't match with implementation. 
- Add all UI changes to SRS. 

### Possible / Future Features ###

* ~~Make the data available to several clients.~~

* Add support for product images. 
* Encrypt sensible data, like Names and passwords. 
* Add option to use the application without the need for an account. This will allow to order food without waiting in line, it will just print a new ticket that will be given to a cashier, who will then introduce the ticket number in the machine and (...) ?