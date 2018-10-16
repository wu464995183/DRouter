
//---------------------------------------------------------------------------------------------------------
//  Techies 指的是dota中的炸弹人, 我希望这个程序能想炸弹人一样可以随处埋雷
//  被注入的view一定要有id.
//  由于是动态注入, 所以导致编译后的代码与源码不一致所以导致不能debug 和 线上应用的crash定位不准
//  关闭注入有一种做法是 在transform中来判断 但是这种方式必须执行copy的操作. 对编译速度会有影响.
//    所以还是将关闭交给用户来做判断
//  理论上mapping能够定位到注入或的代码. 这样基本可以看出crash的所在点. 只是不能按照行号来判断了.待验证...
//
//
//
//         String agentName = 'ime/demo11/PluginAgent'
//        //指定扫描的包名
//        HashSet<String> targetPaths = []
//        //指定扫描的类
//        HashSet<String> targetClass = []
//        //注入的接口类型方法  默认实现了onClick方法
//        String injectIFEvents = '{}'
//        //注入的生命周期类型方法  也可以扫描自定义方法.
//        String injectFAEvents = '{}'
//
//   injectIFEvents例子:
// 'onItemClick',
// '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
// 'android/widget/AdapterView$OnItemClickListener',
// 'onItemClick',
// '(Ljava/lang/String;Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
// 1, 4,
// [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.LLOAD]))

//整合后: {"events":[{"name":"onClick","desc":"(Landroid/view/View;)V","parent":"android/view/View$OnClickListener","agentName":"onClick","agentDesc":"(Ljava/lang/String;Landroid/view/View;)V","paramsStart":1,"paramsCount":1,"opcodes":[25]}]}

//int ILOAD = 21;
//int LLOAD = 22;
//int FLOAD = 23;
//int DLOAD = 24;
//int ALOAD = 25;
//int IALOAD = 46;
//int LALOAD = 47;
//int FALOAD = 48;



//--------------------------------------------------------------------------------------------------------
