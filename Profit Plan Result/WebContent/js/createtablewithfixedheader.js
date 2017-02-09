    function CreateTableFromJSON(json_table_array, tableID) {
    /*function CreateTableFromJSON(json_table_array, containerID, tableID) {*/
        // EXTRACT VALUE FOR HTML HEADER. 
        // ('Book ID', 'Book Name', 'Category' and 'Price')
        var col = [];
        for (var i = 0; i < json_table_array.length; i++) {
            for (var key in json_table_array[i]) {
                if (col.indexOf(key) === -1) {
                    col.push(key);
                }
            }
        }

        // CREATE DYNAMIC TABLE.
        var table = document.getElementById(tableID);
        //Remove all row but exclude header row
        $("#"+tableID+" tr:gt(0)").remove();
/*        table.id = tableID;*/
/*        table.style.tableLayout = "fixed";*/
        // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.

        var tr = null;                   // TABLE ROW.
        
/*        for (var i = 0; i < col.length; i++) {
            var th = document.createElement("th");      // TABLE HEADER.
            th.innerHTML = col[i];
            tr.appendChild(th);
        }*/

        // ADD JSON DATA TO THE TABLE AS ROWS.
  
        for (var i = 0; i < json_table_array.length; i++) {
            tr = table.insertRow(-1);
            for (var j = 0; j < col.length; j++) {
                var tabCell = tr.insertCell(-1);
                tabCell.innerHTML = json_table_array[i][col[j]];
            }
        }

        // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
/*        var divContainer = document.getElementById(containerID);
        divContainer.innerHTML = "";
        divContainer.appendChild(table);*/
    }