<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="utf-8" />
    <title>jqGrid Loading Data - Alternate Row Background</title>
    <!-- The jQuery library is a prerequisite for all jqSuite products -->
    <script type="text/ecmascript" src="/miplataforma/Motor/resources/js/jquery.min.js"></script>

    <!-- We support more than 40 localizations -->
    <script type="text/ecmascript" src="/miplataforma/Motor/resources/js/i18n/grid.locale-es.js"></script>
    <!-- This is the Javascript file of jqGrid -->
    <script type="text/ecmascript" src="/miplataforma/Motor/resources/js/jquery.jqGrid.min.js"></script>
    <!-- This is the localization file of the grid controlling messages, labels, etc.
    <!-- A link to a jQuery UI ThemeRoller theme, more than 22 built-in and many more custom -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/octicons/4.4.0/font/octicons.css">
    <!-- The link to the CSS that the grid needs -->
    <link rel="stylesheet" type="text/css" media="screen" href="/miplataforma/Motor/resources/css/ui.jqgrid-bootstrap4.css" />
	<script>
		$.jgrid.defaults.width = 780;
		$.jgrid.defaults.responsive = true;
		$.jgrid.defaults.styleUI = 'Bootstrap4';
		$.jgrid.defaults.iconSet = "Octicons";
	</script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
<style>
.ui-jqgrid
{
	font-size:0.8rem
}
</style>
</head>
<body>
<div style="margin-left:20px;margin-top:20px">
    <table id="jqGrid"></table>
    <div id="jqGridPager"></div>
	<span class="oi oi-person"  ></span>
</div>
    <script type="text/javascript">

        $(document).ready(function () {
           // altrows are set with table striped class for Boostrap
           //$.jgrid.styleUI.Bootstrap.base.rowTable = "table table-bordered table-striped";

           $("#jqGrid").jqGrid({
                url: '/miplataforma/Motor/rules/0',
                datatype: "json",
                colModel: [
                    { label: 'id', name: 'id', hidden: true, editable: false, key: true },
                    { label: 'idPadre', name: 'id_padre', hidden: true, editable: false },
                    { label: 'Nombre', name: 'nombre', width: 100, sortable: true, editable: true },
                    { label: 'Json', name: 'json', width: 100, editable: true }
                ],
                loadonce: true,
				altRows : true,
				//rownumbers : true,
				//multiselect : true,
                width: 780,
				colMenu : true,
				menubar: true,
				viewrecords : true,
				hoverrows : true,
                height: 200,
                rowNum: 20,
				caption : 'Test',
				sortable: true,
                grouping: true,
                //altRows: true, This does not work in boostrarap
                // altclass: '....'
                pager: "#jqGridPager"
                // set table stripped class in table style in bootsrap
            });
            $('#jqGrid').navGrid('#jqGridPager',
                // the buttons to appear on the toolbar of the grid
                { edit: true, add: true, del: true, search: true, refresh: true, view: true, position: "left", cloneToTop: false },
                // options for the Edit Dialog
                {
                    editCaption: "The Edit Dialog",
                    recreateForm: true,
					checkOnUpdate : true,
					checkOnSubmit : true,
                    closeAfterEdit: true,
                    errorTextFormat: function (data) {
                        return 'Error: ' + data.responseText
                    }
                },
                // options for the Add Dialog
                {
                    closeAfterAdd: true,
                    recreateForm: true,
                    errorTextFormat: function (data) {
                        return 'Error: ' + data.responseText
                    }
                },
                // options for the Delete Dailog
                {
                    errorTextFormat: function (data) {
                        return 'Error: ' + data.responseText
                    }
                },
				{ multipleSearch: true,
				showQuery: true} // search options - define multiple search
				);
			$("#jqGrid").jqGrid('menubarAdd',  [
				{
					id : 'das',
					//cloasoncall : true,
					title : 'Sort by Category',
					click : function ( event) {
						$("#jqGrid").jqGrid('sortGrid','CategoryName');
					}
				},
				{
					divider : true,
				},
				{
					id : 'was',
					//cloasoncall : true,
					title : 'Toggle Visibility',
					click : function ( event) {
						var state = (this.p.gridstate === 'visible') ? 'hidden' : 'visible';
						$("#jqGrid").jqGrid('setGridState',state);
					}
				}
			]);

        });

    </script>



</body>
</html>