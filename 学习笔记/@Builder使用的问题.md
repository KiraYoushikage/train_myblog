# 问题

今天在使用lombok简化model类时。使用@Builder建造者模式。报以下异常
![clipboard.png](https://image-static.segmentfault.com/428/459/428459274-5b419abf2a89e_articlex)
![clipboard.png](https://image-static.segmentfault.com/259/351/2593510581-5b419a846bcba_articlex)

# 解决办法。

1. 去掉@NoArgsConstructor
2. 添加@AllArgsConstructor

# 源码分析

![clipboard.png](https://image-static.segmentfault.com/248/358/2483586996-5b4199fd455bf_articlex)

下图是编译后的源码
![clipboard.png](https://image-static.segmentfault.com/204/593/2045933850-5b419ba35e767_articlex)

只使用@Builder会自动创建全参构造器。而添加上@NoArgsConstructor后就不会自动产生全参构造器



# 1、@Data和@Builder导致无参构造丢失

单独使用@Data注解，是会生成无参数构造方法。

单独使用@Builder注解，发现生成了全属性的构造方法。

@Data和@Builder一起用：我们发现没有了默认的构造方法。如果手动添加无参数构造方法或者用@NoArgsConstructor注解都会报错！

两种解决方法

1、构造方法加上@Tolerate 注解，让lombok假装它不存在（不感知）。

```java
@Builder
@Data
public class TestLombok {

    @Tolerate
    TestLombok() {
    }
    ......
}    

```

2、直接加上这4个注解

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestLombok {
    ......
}    

```

## 2、@Builder注解导致默认值无效



使用Lombok注解可以极高的简化代码量，比较好用的注解除了@Data之外，还有@Builder这个注解，它可以让你很方便的使用builder模式构建对象，但是今天发现@Builder注解会把对象的默认值清掉。



```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestLombok {

private String aa = "zzzz";

public static void main(String[] args) {
    TestLombok build = TestLombok.builder().build();
    System.out.println(build);
}
}
```


输出：TestLombok(aa=null)

解决： 只需要在字段上面加上@Builder.Default注解即可

@Builder.Default
private String aa = "zzzz";

## 3、分析原因

我们使用注解的方式，底层本质就是反射帮我们生成了一系列的setter、getter，所以我们直接打开编译后的target包下面的.class文件，上面的所有原因一目了然！

源文件：

```java

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestLombok {

    private String aa = "zzzz";

    public static void main(String[] args) {
        TestLombok build = TestLombok.builder().build();
        System.out.println(build);
    }
}
```

对应的class字节码：

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.apple.ucar;

public class TestLombok {
    private String aa = "zzzz";

    public static void main(String[] args) {
        TestLombok build = builder().build();
        System.out.println(build);
    }

    public static TestLombok.TestLombokBuilder builder() {
        return new TestLombok.TestLombokBuilder();
    }

    public String getAa() {
        return this.aa;
    }

    public void setAa(String aa) {
        this.aa = aa;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TestLombok)) {
            return false;
        } else {
            TestLombok other = (TestLombok)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$aa = this.getAa();
                Object other$aa = other.getAa();
                if (this$aa == null) {
                    if (other$aa != null) {
                        return false;
                    }
                } else if (!this$aa.equals(other$aa)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof TestLombok;
    }

    public int hashCode() {
        int PRIME = true;
        int result = 1;
        Object $aa = this.getAa();
        int result = result * 59 + ($aa == null ? 43 : $aa.hashCode());
        return result;
    }

    public String toString() {
        return "TestLombok(aa=" + this.getAa() + ")";
    }

    public TestLombok() {
    }

    public TestLombok(String aa) {
        this.aa = aa;
    }

    public static class TestLombokBuilder {
        private String aa;

        TestLombokBuilder() {
        }

        public TestLombok.TestLombokBuilder aa(String aa) {
            this.aa = aa;
            return this;
        }

        public TestLombok build() {
            return new TestLombok(this.aa);
        }

        public String toString() {
            return "TestLombok.TestLombokBuilder(aa=" + this.aa + ")";
        }
    }
}
```

**我们想知道@Data、@Builder等注解底层到底做了什么，直接编译当前文件，即可在生成的.class字节码文件查看具体代码便知道了**

比如上述第二点，采用@Builder的时候，这个aa并没有默认值，所以会为空！！



```java
 public TestLombok.TestLombokBuilder aa(String aa) {
            this.aa = aa;
            return this;
        }
```

## 4、总结

个人觉得如果想要使用@Builder,最简单的方法就是直接写上这4个注解，有默认值的话再加上`@Builder.Default`直接，正常情况下就没啥问题了！



```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestLombok {

	@Builder.Default
    private String aa = "zzzz";

    public static void main(String[] args) {
        TestLombok build = TestLombok.builder().build();
        System.out.println(build);
    }
}

```

————————————————



@Builder

使用起来很好用！！！

但是坑点也很多





