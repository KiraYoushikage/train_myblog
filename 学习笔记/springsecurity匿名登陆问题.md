springsecurity 如果没有登陆，而且接口没有限制访问权限，会以匿名用户的身份进行登陆

所以**SecurityContextHolder.getContext().getAuthentication().getPrincipal()**获得到的只是一个String类型的字符串，值为anyone





例如你直接使用postman发送请求

![image-20210912164557657](E:\项目\train\myblog\学习笔记\assets\springsecurity匿名登陆问题.assets文件夹\image-20210912164557657.png)





这个menu接口是无权限限制的，所以后台会报类型在转换失败

![image-20210912164654817](E:\项目\train\myblog\学习笔记\assets\springsecurity匿名登陆问题.assets文件夹\image-20210912164654817.png)