        $(document).ready(function () {
           // altrows are set with table striped class for Boostrap
           //$.jgrid.styleUI.Bootstrap.base.rowTable = "table table-bordered table-striped";

           $("#jqGrid").jqGrid({
                url: '/miplataforma/Motor/rules/0',
                datatype: "json",
                colModel: [
                    { label: 'id', name: 'id', hidden: true, editable: false, key: true },
                    { label: 'idPadre', name: 'idParent', hidden: true, editable: false },
                    {
                      label: 'Nombre', name: 'rulesetName', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            console.dir(jsonObj);
                            return "<span>" + jsonObj.rulesetName + "</span>";
                        }
                    },
                    {
                      label: 'Tipo', name: 'rulesetType', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            console.dir(jsonObj);
                            return "<span>" + jsonObj.rulesetType + "</span>";
                        }
                    }
                ],
                loadonce: true,
				altRows : true,
				//rownumbers : true,
				//multiselect : true,
				subGrid: true,
                subGridRowExpanded: showGridRuleSet,
                //width: 780,
				colMenu : true,
				menubar: true,
				//viewrecords : true,
				hoverrows : true,
                //height: 200,
                //rowNum: 20,
                rowList: [],
                pgbuttons: false,
                pgtext: null,
                viewrecords: false,
				caption : 'Rule',
				sortable: true,
                //grouping: true,
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


	function showGridRuleSet(parentRowID, parentRowKey) {
	    var childGridID = parentRowID + "_table";
	    var childGridPagerID = parentRowID + "_pager";
	    var childGridURL = "/miplataforma/Motor/rules/" + parentRowKey;

	    $('#' + parentRowID).append('<table id=' + childGridID + '></table><div id=' + childGridPagerID + ' class=scroll></div>');

	    $("#" + childGridID).jqGrid({
	        url: childGridURL,
	        mtype: "GET",
	        datatype: "json",
			subGrid: true,
            subGridRowExpanded: showGridRule,
	        colModel: [
                    { label: 'id', name: 'id', hidden: true, editable: false, key: true },
                    { label: 'idPadre', name: 'idParent', hidden: true, editable: false },
                    {
                      label: 'Nombre', name: 'ruleName', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            console.dir(jsonObj);
                            return "<span>" + jsonObj.rulesetName + "</span>";
                        }
                    },
                    {
                      label: 'Tipo', name: 'rulesetType', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            console.dir(jsonObj);
                            return "<span>" + jsonObj.rulesetType + "</span>";
                        }
                    }
	        ],
			//height: 'auto',
            rowList: [],
            pgbuttons: false,
            pgtext: null,
            viewrecords: false,
	        pager: "#" + childGridPagerID
	    });

	    $("#" + childGridID).jqGrid('navGrid',"#" + childGridPagerID,{add:false,edit:false,del:false,search: false,refresh:false});
	}


	function showGridRule(parentRowID, parentRowKey) {
	    var childGridID = parentRowID + "_table";
	    var childGridPagerID = parentRowID + "_pager";
	    var childGridURL = "/miplataforma/Motor/rules/" + parentRowKey;

	    $('#' + parentRowID).append('<table id=' + childGridID + '></table><div id=' + childGridPagerID + ' class=scroll></div>');

	    $("#" + childGridID).jqGrid({
	        url: childGridURL,
	        mtype: "GET",
	        datatype: "json",
	        colModel: [
                    { label: 'id', name: 'id', hidden: true, editable: false, key: true },
                    { label: 'idPadre', name: 'idParent', hidden: true, editable: false },
                    {
                      label: 'Nombre', name: 'ruleName', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            console.dir(jsonObj);
                            return "<span>" + jsonObj.ruleName + "</span>";
                        }
                    },
                    {
                      label: 'Operador', name: 'operator', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            console.dir(jsonObj);
                            return "<span>" + jsonObj.operator + "</span>";
                        }
                    }
	        ],
			//height: 'auto',
            rowList: [],
            pgbuttons: false,
            pgtext: null,
            viewrecords: false,
	        pager: "#" + childGridPagerID
	    });

	    $("#" + childGridID).jqGrid('navGrid',"#" + childGridPagerID,{add:false,edit:false,del:false,search: false,refresh:false});
	}
