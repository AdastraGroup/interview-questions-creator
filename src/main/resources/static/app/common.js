function isDef(variable) {
        return (typeof variable !== "undefined");
};

function kv(key, value) {
    var a = {};
    a[key] = value;
    return a;
};
function patchUpdate(entityUrl, id, key, newVal, oldVal, doTrim) {

    doTrim = typeof doTrim !== 'undefined' ? doTrim : true; // simulation of default value for function parameter

    if(newVal == oldVal){
        //maybe onUpdateSucces cos user may be confused
        return;
    }

    if(doTrim && typeof newVal == "string"){
        newVal = newVal.trim();
    }

    if(newVal == oldVal){
        this.setState(kv( key, newVal ));
        return;
    }

    var data = kv(key, newVal);
    var url = "/api/" +entityUrl+ "/" + id;

    $.ajax({ url: url, data: JSON.stringify(data), dataType: 'json', type: 'PATCH', headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
             success: function(result)           { this.onUpdateSuccess(id, entityUrl, key, newVal);          }.bind(this),
             error:   function(xhr, status, err) { console.error(url, status, err.toString());
                                                   this.onUpdateFailure(id, entityUrl, key, newVal, oldVal);  }.bind(this)
    });
};