//以下为修改jQuery Validation插件兼容Bootstrap的方法，没有直接写在插件中是为了便于插件升级
$.validator.setDefaults({
	highlight: function(element) {
		$(element).closest('.form-group').removeClass('has-success').addClass('has-error');
	},
	success: function(element) {
		element.closest('.form-group').removeClass('has-error').addClass('has-success');
	},
	errorElement: "span",
	errorPlacement: function(error, element) {
		if(element.is(":radio") || element.is(":checkbox")) {
			error.appendTo(element.parent().parent().parent());
		} else {
			error.appendTo(element.parent());
		}
	},
	errorClass: "help-block m-b-none",
	validClass: "help-block m-b-none"

});

//以下为官方示例
$().ready(function() {
	// validate the comment form when it is submitted
	/*            $("#commentForm").validate();
	 */
	// validate signup form on keyup and submit
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#addUserForm").validate({
		/*onsubmit: true, // 是否在提交是验证
		　　onfocusout: true, // 是否在获取焦点时验证
		　　onkeyup: true, // 是否在敲击键盘时验证*/
		rules: {
			number: "required",
			name: "required",
			sex: "required",
			email:"required",
			phone:"required",
			clazz:"required"
		},
		messages: {
			number: icon + "请输入工/学号",
			name: icon + "请输入姓名",
			sex: icon + "请输入性别",
			email: icon + "请输入邮箱",
			phone: icon + "请输入电话",
			clazz: icon + "请输入班级",
		}
 
	});

	
});