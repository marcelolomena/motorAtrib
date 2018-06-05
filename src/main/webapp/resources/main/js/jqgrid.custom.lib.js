//sip.js
var jqlib = {
    jsonOptions: {
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    },
    createJSON: function (postdata) {
        if (postdata.id === '_empty')
            postdata.id = null;
        return JSON.stringify(postdata)
    },
    dirtyRule: function(json){
        this.json = json
        this.clean = function(propName) {
            var re = new RegExp('^' + propName, 'i'),key;
            var obj = this.json;
            for (key in obj){
                if (re.test(key)){
                    //console.log("borrando [" + key + "]");
                    delete obj[key];
                }
            }

            return obj;
        }
    }
}