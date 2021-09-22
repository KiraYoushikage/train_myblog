

# 坑











>
>
>```java
>  userRoleVO.getRoleIdList().stream().peek(roleId ->{
>            userRoleMapper.insert(UserRole.builder()
>                    .userId(userRoleVO.getUserInfoId())
>                    .roleId(roleId).build());
>        } );
>```
>
>我打算通过stream流来遍历每一个rolesId，并且想在peek方法里面调用userRoleMapper来循环insert
>
>但是结果很奇怪的是，这一步根本没有被执行，也就是说peek方法里面完全没有被执行
>
>这是为啥？



>
>
>初步猜想是应为java的stream流是特殊操作，peek不是一个末端操作，java的stream流必须使用末端操作，整个流才能够跑通
>
>所以这里必须使用末端操作使得peek成功执行。



>
>
>解决方法
>
>```java
>
> userRoleVO.getRoleIdList().stream().peek(roleId ->{
>            userRoleMapper.insert(UserRole.builder()
>                    .userId(userRoleVO.getUserInfoId())
>                    .roleId(roleId).build());
>        } ).forEach(System.out::println);
>```
>
>加个foreach操作
>
>**实际上你这样子写还不如正经地循环insert**
>
>**stream流开销很大的,别搞这些有的没的**

















# 解释

众所周知在Java中使用Stream能够很好地帮我们流处理对象。而Stream中有一个peek方法，它与map最大的区别是它没有返回值。

    一开始我是简单地把它当做一个void类型的处理方法去使用的，但是后来却发现程序执行到此处时，不进peek方法，也就是说peek根本就没有被执行。
    
    后来翻看peek源码，发现里面是这样写的： 


​    

**Returns a stream consisting of the elements of this stream, additionally**
**performing the provided action on each element as elements are consumed**
**from the resulting stream.**

**This is an intermediate operation.**
大意是说，仅在对流内元素进行操作时，peek才会被调用，当不对元素做任何操作时，peek自然也不会被调用了 - -...

最后，用foreach改写了那段代码，问题解决。

 

 

 

PS：其实peek源代码下还备注了一行：

**This method exists mainly to support debugging, where you want to see the elements as they flow past a certain point in a pipeline.**
该方法主要用于调试，方便debug查看Stream内进行处理的每个元素。

好嘛，我承认是我眼瞎了...

 

 

 

最近更新了idea的版本，发现idea居然都有贴心的提示了！





————————————————
版权声明：本文为CSDN博主「Wishes丶」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/acevd/article/details/88219740