<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="author" content="Marcelo Lomeña">
<meta name="description" content="A Rule engine.">
<meta name="keywords" content="rule,rulelist,logic">
<style>
html{margin:0; padding:0; background-color:#e2e3e8;}
body{font-family:"Trebuchet MS", Arial, Helvetica, sans-serif; padding:0 2em; line-height:1.4; background-color:#fcfcfc; width:80%; min-width:20em; max-width:50em; margin:10px auto; border:2px solid #eee;}
@media (max-width:320px){
 body{margin:0; width:auto; min-width:none; max-width:none; margin:0; border:none;}
}
dt{font-weight:bold}
img{vertical-align:middle;}
article, aside, footer, header{display:block; margin:1em 0}
nav{display:block; margin:0}
header a, h1 a, h2 a, h3 a{color:#00e !important; border-bottom:none}
a{text-decoration:none; border-bottom:1px solid}
a:link, cite a:link{color:#00e; border-color:#00e}
a:visited, cite a:visited{color:#551a86; border-color:#551a86}
a:hover{color:darkred!important; background-color:#ffc!important; border-color:darkred!important}
blockquote{padding:0 2em; margin:1.33em 2em; border-left:2em solid #e6e9f9; color:#444}
blockquote address{text-align:right; font-size:smaller}
blockquote address a{color:#898e79}
pre, code, kbd{font-size:1em; font-style:normal; font-weight:normal; font-family:Consolas, Monaco, "Lucida Console", "Liberation Mono", "DejaVu Sans Mono", "Bitstream Vera Sans Mono", "Courier New"}
pre{font-size:small; border:1px solid #e7e7ef; background-color:#f6f6f9; padding:1em}
header{margin-top:0; padding:1ex 0}
body > header{color:#00e; border-bottom:1px solid #00e}
footer{padding:1ex 0; font-size:smaller; margin-bottom:0}
body > footer{border-top:1px solid black; margin-top:3em; font-size:x-small; padding:0; color:#898e79}
article h2{border-bottom:1px solid #ccf}
header h1{font-style:italic; font-size:1.25em; margin:0}
nav ul{list-style:none; text-align:right; margin:0; padding:0}
nav ul.sub-menu{text-align:left;font-size:small}
nav li{display:inline; margin-left:1ex}
header nav li{border-left:2px solid; padding-left:1ex}
nav li:first-child{border-left:none; margin-left:0}
article header{border-bottom:none}
article footer{border-top:none;}
.meta{margin:1em 0 1.33em 1.33em; padding:0; color:#898e79}
.meta a{color:inherit}
.meta cite{color:black; font-style:normal}
.note{background-color:#ffc; padding:0 1em; border:1px solid #eeb}
.note blockquote{border-color:#eeb}
.footnote{font-style:italic}
.error{color:darkred}
/* forms */
fieldset{margin:0; padding:0; border:none}
form.contact fieldset{background-color:#f2f3f8; border:1px solid #e6e9f9; padding:1em}
form.contact fieldset p{margin:0; clear:left}
form.contact p label{display:inline;  float:left;  width:6em;  padding:3px 0}
form.contact p input{display:inline; float:left; width:34em; border:2px solid #e6e9f9}
form.contact textarea{display:inline; float:left; width:40em; margin-top:1em; border:2px solid #e6e9f9}
/* blog */
#weblog-single header h2{color:black}
#prev-next{font-size:smaller}
#prev-next span,#comments .permalink span{display:none}
#prev-next .prev{float:left}
#prev-next .prev:before{content:"\2190"; margin-right:1ex}
#prev-next .next{float:right}
#prev-next .next:after{content:"\2192"; margin-left:1ex}
#prev-next nav:after{content:"\A0"; clear:both}
#comments article{padding:10px 0 20px 40px}
#comments article:after{display:block; content:""; margin:0 auto; border-bottom:1px solid #e6e9f9}
.trackback *{color:#898e79 !important; border-color:#898e79 !important}
#comments .permalink{margin:0 0 -2.33em -40px}
#comments .preview article{padding-top:0; border-left:20px solid #a66}
/* WEBLOG EXTRAS */
blockquote.verse{margin:auto;font-style:italic;font-size:1.1em;font-family:"Times New Roman", Times, serif; text-align:center;width:24em;background-color:ivory;border:1px solid black; color:#631919; padding:1em}
blockquote.verse h4{margin-top:0;text-decoration:underline}
blockquote.verse p{margin:0}
blockquote.verse hr{width:25%;height:1px}
/* syntax highlighting */
.highlight{color:black}
.highlight span{display:inline!important}
.highlight .comment{color:green}
.highlight .conditional{background:#ffffa0}
.highlight .string,.highlight .regexp,.highlight .number{color:darkred}
.highlight .keyword{color:blue}
.highlight .special{color:red}
.highlight .tag{color:purple;font-weight:bold}
.highlight .attribute{font-weight:bold}
.highlight .attribute.value{color:blue;font-weight:normal}
.highlight .entity{color:orangered}
.highlight .processing-instruction{color:orchid;font-style:italic}
/* HTML */
.html .doctype{color:steelblue;font-style:italic}
/* CSS */
.css .comment{color:gray}
.css .selector{color:darkred;font-weight:bold}
.css .at_rule{color:orangered}
.css .property{font-weight:bold}
.css .property.value{color:blue;font-weight:normal}
/* JavaScript */
.javascript .global{color:teal}
.javascript .base2{color:purple}
@media print{
 html{background-color:white}
 body{margin:0; width:auto; min-width:none; max-width:none; font-size:medium; border:none; background-color:white}
 body > header, nav, form{display:none!important}
 header, a{color:black !important}
 a{border:none!important}
 a[href]:after{content:" (" attr(href) ")"; font-size:smaller}
 #home-page a[href]:after, #my-page a[href]:after, .meta a[href]:after, .permalink a[href]:after{content:""}
}



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
<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
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