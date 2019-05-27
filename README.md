# CommonKit
公共组件库

### 基础库
* log :日志相关
* util : 工具相关
* umeng : 友盟相关封装
* bsdiff : 增量文件对比模块
* tinker : 腾讯热修复相关
* social : 友盟登录分享相关
* glide : 基于Glide相关封装
* auto_layout : 与Hongyang开源项目一致

以上Module可单独依赖

### UI库
* widget : 基本组件库，依赖了log\util\autolayout
* refresh_layout : 封装了分页数据加载的SwipeRefreshLayout,老项目在使用，依赖了widget\net

### 架构库
* frame_work : 整合基础库和UI库依赖
* mvvm : 基于frame_work扩展的MVVM开发方式的架构
