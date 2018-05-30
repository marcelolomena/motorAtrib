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
                            //console.dir(rowObject.json);
                            var jsonObj;
                            if(rowObject.json)
                                jsonObj = JSON.parse(rowObject.json);
                            return "<span>" + jsonObj.rulesetName + "</span>";
                        }
                    },
                    {
                      label: 'Tipo', name: 'rulesetType', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.rulesetType + "</span>";
                        }
                    },
                    {
                      label: 'Respuesta', name: 'responseConfig.response', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.responseConfig.response + "</span>";
                        }
                    },
                    {
                      label: 'Clase', name: 'responseConfig.responseClass', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.responseConfig.responseClass + "</span>";
                        }
                    }
                ],
                loadonce: true,
				altRows : true,
				colMenu : true,
				menubar: true,
				hoverrows : true,
                rowList: [],
                pgbuttons: false,
                pgtext: null,
                viewrecords: false,
				caption : 'flujos',
				sortable: true,
                pager: "#jqGridPager",
   				subGrid: true,
                subGridRowExpanded: showGridRuleSet,
                height: "auto",
                autowidth: true,  // set 'true' here
                shrinkToFit: true // well, it's 'true' by default
            });

       	    $('#jqGrid').jqGrid('setGroupHeaders', {
                useColSpanStyle: false,
                    groupHeaders:[
                       	{startColumnName: 'responseConfig.response', numberOfColumns: 2, titleText: '<em>Configuraci\u00F3n</em>'}
                    ]
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
				{ multipleSearch: false,
				showQuery: true} // search options - define multiple search
				);
			$("#jqGrid").jqGrid('menubarAdd',  [
				{
					id : 'das',
					//cloasoncall : true,
					title : 'Ordenar por Nombre',
					click : function ( event) {
						$("#jqGrid").jqGrid('sortGrid','rulesetName');
					}
				},
				{
					divider : true,
				},
				{
					id : 'was',
					//cloasoncall : true,
					title : 'Alternar visibilidad',
					click : function ( event) {
						var state = (this.p.gridstate === 'visible') ? 'hidden' : 'visible';
						$("#jqGrid").jqGrid('setGridState',state);
					}
				}
			]);

            $(window).resize(function () {
                        var outerwidth = $('#grid').width();
                        $('#jqGrid').setGridWidth(outerwidth); // setGridWidth method sets a new width to the grid dynamically
            });

            $(window).unbind('resize.myEvents').bind('resize.myEvents', function () {
                    var outerwidth = $('#grid').width();
                    $('#jqGrid').setGridWidth(outerwidth);
            });


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
                      label: 'Nombre', name: 'rulesetName', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.rulesetName + "</span>";
                        }
                    },
                    {
                      label: 'Tipo', name: 'rulesetType', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.rulesetType + "</span>";
                        }
                    },
                    {
                      label: 'Respuesta', name: 'responseConfig.response', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.responseConfig.response + "</span>";
                        }
                    },
                    {
                      label: 'Clase', name: 'responseConfig.responseClass', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.responseConfig.responseClass + "</span>";
                        }
                    }
	        ],
	        height: "auto",
            autowidth: true,  // set 'true' here
            shrinkToFit: true, // well, it's 'true' by default
            rowList: [],
            pgbuttons: false,
            pgtext: null,
            viewrecords: false,
            caption : 'Condiciones',
	        pager: "#" + childGridPagerID
	    });

   	    $("#" + childGridID).jqGrid('setGroupHeaders', {
                  useColSpanStyle: false,
                  groupHeaders:[
                	{startColumnName: 'responseConfig.response', numberOfColumns: 2, titleText: '<em>Configuraci\u00F3n</em>'}
                  ]
        });

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
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.ruleName + "</span>";
                        }
                    },
                    {
                      label: 'Operador', name: 'operator', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.operator + "</span>";
                        }
                    },
                    {
                      label: 'Variable', name: 'leftParamConfig.parameterName', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.leftParamConfig.parameterName + "</span>";
                        }
                    },
                    {
                      label: 'Clase', name: 'leftParamConfig.parameterClass', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.leftParamConfig.parameterClass + "</span>";
                        }
                    },
                    {
                      label: 'Nombre', name: 'rightParamConfig.parameterName', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.rightParamConfig.parameterName + "</span>";
                        }
                    },
                    {
                      label: 'Clase', name: 'rightParamConfig.parameterClass', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.rightParamConfig.parameterClass + "</span>";
                        }
                    },
                    {
                      label: 'Estática', name: 'rightParamConfig.parameterStaticValue', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.rightParamConfig.parameterStaticValue + "</span>";
                        }
                    },
                    {
                      label: 'Respuesta', name: 'responseConfig.response', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.responseConfig.response + "</span>";
                        }
                    },
                    {
                      label: 'Clase', name: 'responseConfig.responseClass', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            //console.dir(jsonObj);
                            return "<span>" + jsonObj.responseConfig.responseClass + "</span>";
                        }
                    }
	        ],
	        height: "auto",
            autowidth: true,  // set 'true' here
            shrinkToFit: true, // well, it's 'true' by default
            rowList: [],
            pgbuttons: false,
            pgtext: null,
            caption : 'Reglas',
            viewrecords: false,
	        pager: "#" + childGridPagerID
	    });

	    $("#" + childGridID).jqGrid('setGroupHeaders', {
          useColSpanStyle: false,
          groupHeaders:[
        	{startColumnName: 'leftParamConfig.parameterName', numberOfColumns: 2, titleText: '<em>Izquierda</em>'},
        	{startColumnName: 'rightParamConfig.parameterName', numberOfColumns: 3, titleText: '<em>Derecha</em>'},
        	{startColumnName: 'responseConfig.response', numberOfColumns: 2, titleText: '<em>Configuraci\u00F3n</em>'}
          ]
        });

	    $("#" + childGridID).jqGrid('navGrid',"#" + childGridPagerID,{add:false,edit:false,del:false,search: false,refresh:false});
	}
