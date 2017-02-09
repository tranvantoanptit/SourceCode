var builddata = function (data) {
    var source = [];
    var items = [];
    var id = '';
    var label='';
    // build hierarchical source.
    for (i = 0; i < data.length; i++) {
        var item = data[i];
        // var label = item["org_cd"]+":"+item["org_name_1"];
        if(Number(item["org_cd"])!= 0){
        	label = item["org_cd"];
        	id = item["org_cd"];
        }
        else {
        	label = 'KVC';
        	id = 0;
        }
        
        if(Number(item["upper_rank_org_cd"])!=0)
        	parentid = item["upper_rank_org_cd"];
        else
        	parentid = 0;

        if (items[parentid]) {
            var item = { parentid: parentid, label: label, item: item };
            if (!items[parentid].items) {
                items[parentid].items = [];
            }
            items[parentid].items[items[parentid].items.length] = item;
            items[id] = item;
        }
        else {
            items[id] = { parentid: parentid, label: label, item: item };
            source[id] = items[id];
        }
    }
    return source;
}