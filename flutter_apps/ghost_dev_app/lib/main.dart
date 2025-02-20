import 'package:flutter/material.dart';
import 'package:ghost_dev_app/screens/home_screen.dart';

void main() {
  runApp(const App());
}

class App extends StatelessWidget {
  const App({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Ghost Dev',
      debugShowCheckedModeBanner: false,
      home: const HomeScreen(),
    );
  }
}
