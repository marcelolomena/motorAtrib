<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="author" content="Marcelo Lomeña">
<meta name="description" content="A Rule engine.">
<meta name="keywords" content="rule,rulelist,logic">
<link rel="stylesheet" href="/miplataforma/Motor/resources/main/css/style.css">
<style>
h2 {margin: 0.5em 0em; font-weight: normal; font-size: 1em;}
hr {clear: both;}
.version {float: right;}
.description {float: left;}
form {padding: 0; margin: 0;}
textarea {margin: 0;
 white-space: pre-wrap;       /* css-3 */
 white-space: -moz-pre-wrap;  /* Mozilla, since 1999 */
 white-space: -pre-wrap;      /* Opera 4-6 */
 white-space: -o-pre-wrap;    /* Opera 7 */
 word-wrap: break-word;       /* Internet Explorer 5.5+ */
}
#form textarea {clear: both; width: 98%; margin-top: 4px; padding: 0 2px; font-family: monospace;}
@media screen and (min-width: 0px) {#form textarea{width: 100%;}}
textarea.disabled, .disabled textarea {background-color: #EEEEEE; border: 2px solid #898E79;}
*.disabled, .disabled * {color: #898E79 !important;}
#output {background-color: #F0F0F0; border-color: #CCCCCC;}
#controls {float: right; text-align: right;}
#controls select {font-size: 0.83em; vertical-align: top;}
#message {font-size: x-small; margin-top: 0.5em;}
.error {color: #AA3333;}
#form #message {margin: 0 0 1em 0; float: right;}
#help {float: right;}
#form p {margin: 0;}
#form p.form-buttons, #form #controls {margin: 1em 0;}
p.form-buttons button {margin-right: 0.2em;}
.menu {display: none;}
#disabled {font-weight: bold; text-align: center; display: none; color: #000000 !important;}
.disabled #disabled {display: block;}
#upload-script {display: none;}
p.footnote {margin-bottom: 0; float: left;}
#load-script, #save-script {display: none;}
</style>
<style>
/* Popup box BEGIN */
.hover_bkgr_fricc{
    background:rgba(0,0,0,.4);
    cursor:pointer;
    display:none;
    height:100%;
    position:fixed;
    text-align:center;
    top:0;
    width:100%;
    z-index:10000;
}
.hover_bkgr_fricc .helper{
    display:inline-block;
    height:100%;
    vertical-align:middle;
}
.hover_bkgr_fricc > div {
    background-color: #fff;
    box-shadow: 10px 10px 60px #555;
    display: inline-block;
    height: auto;
    max-width: 551px;
    min-height: 100px;
    vertical-align: middle;
    width: 60%;
    position: relative;
    border-radius: 8px;
    padding: 15px 5%;
}
.popupCloseButton {
    background-color: #fff;
    border: 3px solid #999;
    border-radius: 50px;
    cursor: pointer;
    display: inline-block;
    font-family: arial;
    font-weight: bold;
    position: absolute;
    top: -20px;
    right: -20px;
    font-size: 25px;
    line-height: 30px;
    width: 30px;
    height: 30px;
    text-align: center;
}
.popupCloseButton:hover {
    background-color: #ccc;
}
.trigger_popup_fricc {
    cursor: pointer;
}
/* Popup box BEGIN */
</style>
<script src="/miplataforma/Motor/resources/jquery/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript">

$(document).ready(function(){

	$("#form").removeClass( "disabled" );
	$("#test-script").removeClass( "disabled" );
	$("#clear-all").prop('disabled',false);
	$("#input").prop('disabled',false);
	$("#input").focus();

    $(".trigger_popup_fricc").click(function(){
       $('.hover_bkgr_fricc').show();
    });
    $('.hover_bkgr_fricc').click(function(){
        $('.hover_bkgr_fricc').hide();
    });
    $('.popupCloseButton').click(function(){
        $('.hover_bkgr_fricc').hide();
    });


	var datum = {
                    "info": {
                        "action":"123",
                        "text1":""
                    }
            };

    $("#test-script").click(function() {
        event.preventDefault();
        var post_url = $("#form").attr("action");
        var request_method = $("#form").attr("method");
        datum.info.text1 = $("#input").val();
        $.ajax({
            url : post_url,
            type: request_method,
            data : $("#input").val(),
            contentType: "application/json; charset=utf-8",
            success: function(response) {
                $("#output").val(response);
            }
        });
    });

    $("#clear-all").click(function() {
           $('#input').val('');
           $('#output').val('');
    });

});
</script>
</head>
<body>

<h2><span class="description">A Test Rule.</span> <span class="version">version 1.0</span></h2>
<hr class="short">
<form action="/miplataforma/Motor/testing" class="disabled" id="form" method="post">
 <p id="help"><a class="trigger_popup_fricc">Help</a></p>
 <p><label class="paste">Paste:</label><br>
  <textarea id="input" name="input" rows="10" cols="80" disabled></textarea></p>
 <p id="controls">
  <label for="base62">Rule
   <input type="checkbox" id="rule" name="rule" value="1" disabled></label><br>
  <label for="shrink">RuleList
   <input type="checkbox" id="rulelist" name="rulelist" value="1" disabled checked></label>
 </p>
 <p class="form-buttons" id="input-buttons">
  <button type="button" id="clear-all" disabled>Clear</button>
  <button type="button" id="test-script">Test</button>
 </p>
 <p><label class="copy">Result:</label>
  <textarea id="output" name="output" rows="10" cols="80" readonly disabled></textarea></p>
 <p id="message" class="error">disabled</p>
 <p class="form-buttons" id="output-buttons">
  <button type="submit" id="save-script" disabled>Save</button>
 </p>
</form>

<footer>
 <p>Copyright © 2008-2018 Zrismart.cl. All rights reserved.</p>
</footer>
<div class="hover_bkgr_fricc">
	<span class="helper"></span>
	<div>
		<div class="popupCloseButton">X</div>
		<p>Enter json with variables to evaluate</p>
	</div>
</div>

</body>
</html>