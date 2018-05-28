<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title>jqGrid Loading Data - Alternate Row Background</title>
    <script src="/miplataforma/Motor/resources/jquery/jquery.min.js" type="text/javascript"></script>
    <script type="text/ecmascript" src="/miplataforma/Motor/resources/jqgrid/js/i18n/grid.locale-es.js"></script>
    <script type="text/ecmascript" src="/miplataforma/Motor/resources/jqgrid/js/jquery.jqGrid.min.js"></script>
	<link rel="stylesheet" href="/miplataforma/Motor/resources/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/octicons/4.4.0/font/octicons.css">
    <link rel="stylesheet" type="text/css" media="screen" href="/miplataforma/Motor/resources/jqgrid/css/ui.jqgrid-bootstrap4.css" />
	<script>
		$.jgrid.defaults.width = 800;
		$.jgrid.defaults.responsive = true;
		$.jgrid.defaults.styleUI = 'Bootstrap4';
		$.jgrid.defaults.iconSet = "Octicons";
	</script>
    <script src="/miplataforma/Motor/resources/popper/js/popper.min.js" type="text/javascript"></script>
    <script src="/miplataforma/Motor/resources/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="/miplataforma/Motor/resources/main/js/admin.js" type="text/javascript"></script>
    <style>
    .ui-jqgrid
    {
        font-size:0.8rem
    }
    </style>
</head>
<body>
<div class="container">
<div style="margin-left:20px;margin-top:20px">
    <table id="jqGrid"></table>
    <div id="jqGridPager"></div>
</div>
</div>
</body>
</html>