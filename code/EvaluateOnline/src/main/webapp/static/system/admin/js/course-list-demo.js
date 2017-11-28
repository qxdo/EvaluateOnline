// Example Bootstrap Table Events
// ------------------------------
(function() {
	$('#courseList').bootstrapTable({
		url: "bootstrap_table_test.json",/*111*/
		search: true,
		pagination: true,
		showRefresh: true,
		showToggle: true,
		showColumns: true,
		iconSize: 'outline',
		toolbar: '#courseToolbar',
		icons: {
			refresh: 'glyphicon-repeat',
			toggle: 'glyphicon-list-alt',
			columns: 'glyphicon-list'
		},
		columns:[{  
	            field: 'listnumber',//可不加  
	            title: '序号',//标题  可不加  
	            formatter: function (value, row, index) {  
	                return index+1; 
	            }  
	        },{  
	            checkbox: false 
        	}]  
	});

})();
/*表格行增加删除函数*/
function actionFormatter(value, row, index) {
	return '<a class="mod" >修改</a> ' + '<a class="delete">删除</a>';
}
/*增加分配函数*/
function distribFormatter(value, row, index) {
	return '<a class="distrib">分配</a> ';
}

//表格  - 操作 - 事件
window.actionEvents = {
	'click .mod': function(e, value, row, index) {//修改
		//修改操作
		/*console.log("工号：" + row.column1);
		location.href = "course_tree.html"*/
		var number = row.number;
		console.log(number);
		
		document.location.href = "/admin/course/update?number="+number;
	},
	'click .delete': function(e, value, row, index) {//删除
		var id = row.id;
		if(!confirm("确认要删除吗？")) {
			return;
		}
		
		var number = row.number;
		console.log(number);
		
		$.ajax({
    		type: "POST",
			url: "/admin/course/delete",
			data: {number: number},
			dataType: "JSON",
			success:function(data){
				if(data.code==1){
                    //删除成功
                    $("#success").show();
                    $("#courseList").bootstrapTable('remove', {
						field: 'id',
						values: [parseInt(id)]
					});
					setTimeout(function(){
						$("#success").hide();
					},2000);
				}else{
                   $("#fail").show();
					setTimeout(function(){
						$("#fail").hide();
					},2000);
				}
			}
    	});
	}
}
window.distribEvents={
	'click .distrib': function(e, value, row, index) {
		var id = row.id;
		location.href="distribute.html?type=1&id="+id;
	}
}