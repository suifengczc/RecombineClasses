# RecombineClasses介绍
该工程是实现了将Java项目中的所有方法打乱重组，重新分配到任意类中。使得相同的功能代码有不同的结构

# 实现原理
该工程使用了[JavaParser库](https://github.com/javaparser/javaparser),
JavaParser可以根据传入的代码输出一个抽象语法树，在抽象语法树中可以实现遍历所有方法，生成新类并插入方法的功能

# 项目结构
在operation下提供了几个操作类，包括对类操作，方法操作等
在support下提供了需要用到的数据类，config类，工具类等
具体操作流程在Main类中实现。

