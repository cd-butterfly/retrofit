retrofit 
========

基于[Retrofit2][1]的网络请求框架 

Download
--------

grab via Maven:
```xml
<dependency>
  <groupId>com.github.cd-butterfly</groupId>
  <artifactId>retrofit</artifactId>
  <version>1.0.4-SNAPSHOT</version>
  <type>aar</type>
</dependency>

<dependency>
  <groupId>com.github.cd-butterfly</groupId>
  <artifactId>retrofit-compiler</artifactId>
  <version>1.0.4-SNAPSHOT</version>
</dependency>

```
or Gradle:

project build.gradle :

```groovy
repositories {
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}
```

module build.gradle

```groovy
implementation 'com.github.cd-butterfly:retrofit:1.0.4-SNAPSHOT'
annotationProcessor  'com.github.cd-butterfly:retrofit-compiler:1.0.4-SNAPSHOT'
```

License
=======

    Copyright (c) 2018 cd_butterfly
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://square.github.io/retrofit/
