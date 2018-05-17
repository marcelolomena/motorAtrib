<!DOCTYPE html>
<html>
<head>
<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
<script type="text/javascript">

$(document).ready(function(){
    $("#my_form").submit(function(event){
        var datum = {
                "info": {
                    "id":"123",
                    "text1":""
                }
        };

        event.preventDefault(); //prevent default action
        var post_url = $(this).attr("action"); //get form action url
        var request_method = $(this).attr("method"); //get form GET/POST method
        datum.info.text1 = $("#json").val();

        $.ajax({
            url : post_url,
            type: request_method,
            data : datum,
            contentType: "application/json; charset=utf-8"
        }).done(function(response){
            $("#server-results").html(response);
        });
    });
});
</script>
</head>
<body>

<form action="/miplataforma/Motor/testing" method="post" id="my_form">
    <label>JSON</label>
    <textarea rows="25" cols="50" id="json"></textarea>
    <input type="submit" name="submit" value="Submit Form" />
</form>
<div id="server-results" />
</body>
</html>