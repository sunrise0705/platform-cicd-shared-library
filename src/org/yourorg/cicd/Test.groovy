package org.yourorg.cicd

class Test {
  static void runUnitTests(Map c) {
    println "[Test] Run unit tests (runtime-specific steps can be added)"
    // 예: c.runtime == 'java' 이면 mvn test, node면 npm test 등
  }
}
