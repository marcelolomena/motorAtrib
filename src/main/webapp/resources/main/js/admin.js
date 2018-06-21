        $(document).ready(function () {
           $("#jqGrid").jqGrid({
                url: '/miplataforma/Motor/rules/0',
                datatype: "json",
                colModel: [
                    { label: 'id', name: 'id', hidden: true, editable: false, key: true },
                    { label: 'idPadre', name: 'idParent', hidden: true, editable: false },
                    {
                      label: 'Nombre', name: 'rulesetName', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
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
                            return "<span>" + jsonObj.rulesetType + "</span>";
                        }
                    },
                    {
                      label: 'Respuesta', name: 'responseConfig.response', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
                            var jsonObj = JSON.parse(rowObject.json);
                            return "<span>" + jsonObj.responseConfig.response + "</span>";
                        }
                    },
                    {
                      label: 'Clase', name: 'responseConfig.responseClass', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
                            var jsonObj = JSON.parse(rowObject.json);
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
                autowidth: true,
                shrinkToFit: true
            });

       	    $('#jqGrid').jqGrid('setGroupHeaders', {
                useColSpanStyle: false,
                    groupHeaders:[
                       	{startColumnName: 'responseConfig.response', numberOfColumns: 2, titleText: '<em>Configuraci\u00F3n</em>'}
                    ]
            });

            $('#jqGrid').navGrid('#jqGridPager',
                { edit: false, add: false, del: false, search: false, refresh: true, view: true, position: "left", cloneToTop: false },
                {
                    editCaption: "Modificar",
                    recreateForm: true,
					checkOnUpdate : true,
					checkOnSubmit : true,
                    closeAfterEdit: true
                },
                {
                    closeAfterAdd: true,
                    recreateForm: true
                },
                {
                },
				{ multipleSearch: false,
				showQuery: true}
				);
                $("#jqGrid").jqGrid('menubarAdd',  [
                    {
                        id : 'das',
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
                        title : 'Alternar visibilidad',
                        click : function ( event) {
                            var state = (this.p.gridstate === 'visible') ? 'hidden' : 'visible';
                            $("#jqGrid").jqGrid('setGridState',state);
                        }
                    }
                ]);

                $(window).resize(function () {
                            var outerwidth = $('#grid').width();
                            $('#jqGrid').setGridWidth(outerwidth);
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
                            return "<span>" + jsonObj.rulesetName + "</span>";
                        }
                    },
                    {
                      label: 'Tipo', name: 'rulesetType', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
                            var jsonObj = JSON.parse(rowObject.json);
                            return "<span>" + jsonObj.rulesetType + "</span>";
                        },
                        edittype: "select",
                        editoptions:{ value: "BooleanArray:BooleanArray;FirstTrue:FirstTrue" }
                    },
                    {
                      label: 'Respuesta', name: 'responseConfig[response]', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
                            var jsonObj = JSON.parse(rowObject.json);
                            return jsonObj.responseConfig.response;
                        }
                    },
                    {
                      label: 'Clase', name: 'responseConfig[responseClass]', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
                            var jsonObj = JSON.parse(rowObject.json);
                            return jsonObj.responseConfig.responseClass;
                        },
                        edittype: "select",
                        editoptions:{ value: "Boolean:Boolean;String:String" }
                    }
	        ],
	        height: "auto",
			altRows : true,
            autowidth: true,
            shrinkToFit: true,
            rowList: [],
            pgbuttons: false,
            pgtext: null,
            viewrecords: true,
            caption : 'Condiciones',
	        pager: "#" + childGridPagerID,
   	        editurl:'/miplataforma/Motor/uRuleSet'
	    });

   	    $("#" + childGridID).jqGrid('setGroupHeaders', {
              useColSpanStyle: false,
              groupHeaders:[
                	{startColumnName: 'responseConfig[response]', numberOfColumns: 2, titleText: '<em>Configuraci\u00F3n</em>'}
              ]
        });

        $("#" + childGridID).navGrid("#" + childGridPagerID,
            { edit: true, add: true, del: true, search: false, refresh: true, view: true, position: "left", cloneToTop: false },
            {
                editCaption: "Modificar Conjunto de reglas",
                ajaxEditOptions: jqlib.jsonOptions,
                serializeEditData: jqlib.createJSON,
                recreateForm: true,
				checkOnUpdate : true,
				checkOnSubmit : true,
                closeAfterEdit: true,
                beforeSubmit : function( postdata, formid ) {
                    var pd1 = new jqlib.dirtyRule(postdata);
                    postdata=pd1.clean("rulesetName");
                    var pd2 = new jqlib.dirtyRule(postdata);
                    postdata=pd2.clean("rulesetType");
                    var pd3 = new jqlib.dirtyRule(postdata);
                    postdata=pd3.clean("responseConfig");

                    var json = $('form').serializeJSON();

                    var dr = new jqlib.dirtyRule(json);
                    var jsonp=dr.clean("jqGrid_");
                    postdata.json=JSON.stringify(jsonp);
                    return [true,''];
                },
                afterSubmit: function (response, postdata) {
                    var res = $.parseJSON(response.responseText);
                    if (res.pestado != -1) {
                        return [true,"",""]
                    } else {
                        return [false, res.pglosa, ""]
                    }
                }
            },
            {
                addCaption: "Agregar conjunto de reglas",
                closeAfterAdd: true,
                recreateForm: true,
                ajaxEditOptions: jqlib.jsonOptions,
                serializeEditData: jqlib.createJSON,
                beforeSubmit : function( postdata, formid ) {
                    var pd1 = new jqlib.dirtyRule(postdata);
                    postdata=pd1.clean("rulesetName");
                    var pd2 = new jqlib.dirtyRule(postdata);
                    postdata=pd2.clean("rulesetType");

                    var pd3 = new jqlib.dirtyRule(postdata);
                    postdata=pd3.clean("responseConfig");

                    var json = $('form').serializeJSON();

                    var dr = new jqlib.dirtyRule(json);
                    var jsonp=dr.clean("jqGrid_");

                    postdata.id=parentRowKey;
                    postdata.json=JSON.stringify(jsonp);
                    return [true,''];
                },
                afterSubmit: function (response, postdata) {
                    var res = $.parseJSON(response.responseText);
                    if (res.pestado != -1) {
                        return [true,"",""]
                    } else {
                        return [false, res.pglosa, ""]
                    }
                }
            },
            {
                reloadAfterSubmit:true,
                errorTextFormat: function (data) {
                    return 'Error: ' + data.responseText
                },
                ajaxDelOptions:jqlib.jsonOptions,
                serializeDelData:jqlib.createJSON,
                onclickSubmit: function(params, postdata){
                    var ruledata = {
                        "json":""
                    }
                    return ruledata;
                }
            },
			{
			    multipleSearch: false,
			    showQuery: true
		    }
		);
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
                    { label: 'idParent', name: 'idParent', hidden: true, editable: false, },
                    {
                      label: 'Nombre', name: 'ruleName', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
                            var jsonObj = JSON.parse(rowObject.json);
                            return jsonObj.ruleName;
                        }
                    },
                    {
                      label: 'Operador', name: 'operator', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {

                            var jsonObj = JSON.parse(rowObject.json);
                            return jsonObj.operator;
                        },
                        edittype: "select",
                        editoptions:{ value: "GT:GT;LT:LT;GTE:GTE;LTE:LTE;EQ:EQ;NEQ:NEQ;IN:IN" }
                    },
                    {
                      label: 'Variable', name: 'leftParamConfig[parameterName]', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
                            var jsonObj = JSON.parse(rowObject.json);
                            return jsonObj.leftParamConfig.parameterName;
                        },
                        edittype: "select",
                        editoptions: {
                            dataUrl: '/miplataforma/Motor/variables',
                                buildSelect: function (response) {
                                    var data = JSON.parse(response);
                                    var s = "<select>";
                                    s += '<option value="0">--Escoger La Variable--</option>';
                                    $.each(data, function(i, item) {
                                            s += '<option value="' + data[i].nombre + '">' + data[i].nombre + '</option>';
                                    });
                                    return s + "</select>";
                                }
                        }
                    },
                    {
                      label: 'Clase', name: 'leftParamConfig[parameterClass]', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
                            var jsonObj = JSON.parse(rowObject.json);
                            return jsonObj.leftParamConfig.parameterClass;
                        },
                        edittype: "select",
                        editoptions:{ value: "Boolean:Boolean;Double:Double;Long:Long;String:String;NumberSet:NumberSet;DateTime:DateTime" }
                    },
                    {
                      label: 'Nombre', name: 'rightParamConfig[parameterName]', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
                            var jsonObj = JSON.parse(rowObject.json);
                            return jsonObj.rightParamConfig.parameterName;
                        }
                    },
                    {
                      label: 'Clase', name: 'rightParamConfig[parameterClass]', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
                            var jsonObj = JSON.parse(rowObject.json);
                            return jsonObj.rightParamConfig.parameterClass;
                        },
                        edittype: "select",
                        editoptions:{ value: "Boolean:Boolean;Double:Double;Long:Long;String:String;NumberSet:NumberSet;DateTime:DateTime" }
                    },
                    {
                      label: 'Est\u00E1tica', name: 'rightParamConfig[parameterStaticValue]', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
                            var jsonObj = JSON.parse(rowObject.json);
                            return jsonObj.rightParamConfig.parameterStaticValue;
                        }
                    },
                    {
                      label: 'Respuesta', name: 'responseConfig[response]', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
                            var jsonObj = JSON.parse(rowObject.json);
                            return jsonObj.responseConfig.response;
                        }
                    },
                    {
                      label: 'Clase', name: 'responseConfig[responseClass]', width: 100,editable: true,
                        formatter: function (cellvalue, options, rowObject) {
                            var jsonObj = JSON.parse(rowObject.json);
                            return jsonObj.responseConfig.responseClass;
                        },
                        edittype: "select",
                        editoptions:{ value: "Boolean:Boolean;String:String" }
                    }
	        ],
	        height: "auto",
	        altRows : true,
            autowidth: true,
            shrinkToFit: true,
            rowList: [],
            pgbuttons: false,
            pgtext: null,
            caption : 'Reglas',
            viewrecords: true,
	        pager: "#" + childGridPagerID,
	        editurl:'/miplataforma/Motor/uRule'
	    });

	    $("#" + childGridID).jqGrid('setGroupHeaders', {
          useColSpanStyle: false,
          groupHeaders:[
        	{startColumnName: 'leftParamConfig[parameterName]', numberOfColumns: 2, titleText: '<em>Izquierda</em>'},
        	{startColumnName: 'rightParamConfig[parameterName]', numberOfColumns: 3, titleText: '<em>Derecha</em>'},
        	{startColumnName: 'responseConfig[response]', numberOfColumns: 2, titleText: '<em>Configuraci\u00F3n</em>'}
          ]
        });

        $("#" + childGridID).navGrid("#" + childGridPagerID,
            { edit: true, add: true, del: true, search: false, refresh: true, view: true, position: "left", cloneToTop: false },
            {
                editCaption: "Modificar Regla",
                ajaxEditOptions: jqlib.jsonOptions,
                serializeEditData: jqlib.createJSON,
                recreateForm: true,
				checkOnUpdate : true,
				checkOnSubmit : true,
                closeAfterEdit: true,
                beforeSubmit : function( postdata, formid ) {
                    var pd1 = new jqlib.dirtyRule(postdata);
                    postdata=pd1.clean("operator");
                    var pd2 = new jqlib.dirtyRule(postdata);
                    postdata=pd2.clean("ruleName");
                    var pd3 = new jqlib.dirtyRule(postdata);
                    postdata=pd3.clean("leftParamConfig");
                    var pd4 = new jqlib.dirtyRule(postdata);
                    postdata=pd4.clean("responseConfig");
                    var pd5 = new jqlib.dirtyRule(postdata);
                    postdata=pd5.clean("rightParamConfig");

                    var json = $('form').serializeJSON();

                    var dr = new jqlib.dirtyRule(json);
                    var jsonp=dr.clean("jqGrid_");
                    postdata.json=JSON.stringify(jsonp);
                    return [true,''];
                },
                afterSubmit: function (response, postdata) {
                    var res = $.parseJSON(response.responseText);
                    if (res.pestado != -1) {
                        return [true,"",""]
                    } else {
                        return [false, res.pglosa, ""]
                    }
                }
            },
            {
                addCaption: "Agregar Regla",
                closeAfterAdd: true,
                recreateForm: true,
                ajaxEditOptions: jqlib.jsonOptions,
                serializeEditData: jqlib.createJSON,
                errorTextFormat: function (data) {
                    return 'Error: ' + data.responseText
                },
                beforeSubmit : function( postdata, formid ) {
                    var pd1 = new jqlib.dirtyRule(postdata);
                    postdata=pd1.clean("operator");
                    var pd2 = new jqlib.dirtyRule(postdata);
                    postdata=pd2.clean("ruleName");
                    var pd3 = new jqlib.dirtyRule(postdata);
                    postdata=pd3.clean("leftParamConfig");
                    var pd4 = new jqlib.dirtyRule(postdata);
                    postdata=pd4.clean("responseConfig");
                    var pd5 = new jqlib.dirtyRule(postdata);
                    postdata=pd5.clean("rightParamConfig");

                    var json = $('form').serializeJSON();

                    var dr = new jqlib.dirtyRule(json);
                    var jsonp=dr.clean("jqGrid_");

                    postdata.id=parentRowKey;
                    postdata.json=JSON.stringify(jsonp);
                    return [true,''];
                },
                afterSubmit: function (response, postdata) {
                    var res = $.parseJSON(response.responseText);
                    if (res.pestado != -1) {
                        return [true,"",""]
                    } else {
                        return [false, res.pglosa, ""]
                    }
                }
            },
            {
                reloadAfterSubmit:true,
                ajaxDelOptions:jqlib.jsonOptions,
                serializeDelData:jqlib.createJSON,
                onclickSubmit: function(params, postdata){
                    var ruledata = {
                        "json":""
                    }
                    return ruledata;
                }
            },
			{
			    multipleSearch: false,
			    showQuery: true
		    }
		);

	}