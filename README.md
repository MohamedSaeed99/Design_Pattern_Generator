# Homework 3
### Description: object-oriented design and implementation of the name clashing functionality in the IntelliJ plugin with the design pattern code generator from homeworks 1 and 2.
### Grade: 8% + bonus up to 3% for using a Java parser (it can be Eclipse AST parser or the module [jdk.compiler](https://docs.oracle.com/en/java/javase/13/docs/api/jdk.compiler/module-summary.html)).
#### You can obtain this Git repo using the command git clone git clone git@bitbucket.org:cs474_spring2020/homework3.git.

## Preliminaries
As part of the previous homework assignments you gained experience with creating and managing your Git repositories, you have learned many design patterns, you created your model and the object-oriented design of a design pattern code generator, you learned to create JUnit or Cucumber or FlatSpec tests, you created your SBT or Gradle build scripts, and you completed your first IntelliJ plugin! Congratulations!

If you haven't done it, it is your chance to catch up with the missed opportunities and deliver it in this homework for a small percentage applied to the previous homeworks.

If you have done your first homework, you can skip the rest of the prelimininaries. Your instructor created a team for this class named CS474_Spring2020. Please contact your TA, [Mr. Mohammed Siddiq](msiddi56@uic.edu) using your UIC.EDU email account and he will add you to the team repo as developers, since Mr.Siddiq already has the admin privileges. Please use your emails from the class registration roster to add you to the team and you will receive an invitation from BitBucket to join the team. Since it is a large class, please use your UIC email address for communications or Piazza and avoid emails from other accounts like funnybunny1998@gmail.com. If you don't receive a response within 24 hours, please contact us via Piazza, since it may be a case that your direct emails went to the spam folder.

If you haven't already done so, please create create your account at [BitBucket](https://bitbucket.org/), a Git repo management system. It is imperative that you use your UIC email account that has the extension @uic.edu. Once you create an account with your UIC address, BibBucket will assign you an academic status that allows you to create private repos. Bitbucket users with free accounts cannot create private repos, which are essential for submitting your homeworks and the course project. If you have a problem with obtaining the academic account with your UIC.EDU email address, please contact Atlassian's license and billing team and ask them to enable your academic account by filling out the [Atlassian Bitbucket academic account request form](https://www.atlassian.com/software/views/bitbucket-academic-license).

Next, if you haven't done so, you will install [IntelliJ](https://www.jetbrains.com/student/) with your academic license, the JDK, the Scala runtime and the IntelliJ Scala plugin, the [Simple Build Toolkit (SBT)](https://www.scala-sbt.org/1.x/docs/index.html) or the [Gradle build tool](https://gradle.org/) and make sure that you can create, compile, and run Java and Scala programs. Please make sure that you can run [various Java tools from your chosen JDK](https://docs.oracle.com/en/java/javase/index.html).

Just to remind you, in this like all other homeworks and in the course project you will use logging and configuration management frameworks. You will comment your code extensively and supply logging statements at different logging levels (e.g., TRACE, INFO, ERROR) to record information at some salient points in the executions of your programs. All input and configuration variables must be supplied through configuration files -- hardcoding these values in the source code is generally prohibited and will be punished by taking a large percentage of points from your total grade! You are expected to use [Logback](https://logback.qos.ch/) and [SLFL4J](https://www.slf4j.org/) for logging and [Typesafe Conguration Library](https://github.com/lightbend/config) for managing configuration files. These and other libraries should be imported into your project using your script [build.sbt](https://www.scala-sbt.org/1.0/docs/Basic-Def-Examples.html) or [gradle script](https://docs.gradle.org/current/userguide/writing_build_scripts.html). These libraries and frameworks are widely used in the industry, so learning them is the time well spent to improve your resumes.

## Functionality
In this third and the last homework in CS474 this Spring 2020, you will create an extension of your basic plugin for IntelliJ that you created in homework 2 where you enabled software engineers to generate code for a user-chosen design pattern and add it to the existing Java project opened and loaded in IntelliJ. In this homework, you will add a checker  to your IntelliJ plugin for name clash errors, which result from two or more identical types being declared in the same scope. When the user of your plugin selects a desired design patterns and specifies the names for classes/interfaces or some other types for this selected design pattern, these names may clash with the names of already existing types in the open IntelliJ project. The goal of this homework is for you to extend the functionality of your plugin to analyze the types in the existing open IntelliJ project, to determine name clashes with your generated design pattern, and to issue an error to the software engineer prompting her/him to change the clashing name(s).

You should start off by reading the IntelliJ Plugin SDK documentation -- [Part III: Project Model](https://www.jetbrains.org/intellij/sdk/docs/reference_guide/project_model/project.html) and [Part IV: Program Structure Interface (PSI)](https://www.jetbrains.org/intellij/sdk/docs/basics/architectural_overview/psi.html). 

As usual, this homework script is written using a retroscripting technique, in which the homework outlines are generally and loosely drawn, and the individual students improvise to create the implementation that fits their refined objectives. In doing so, students are expected to stay within the basic requirements of the homework and they are free to experiments. That is, it is impossible that two non-collaborating students will submit similar homeworks! Asking questions is important, **so please ask away on Piazza!**

Your homework can be divided roughly into five steps. First, you learn the plugin's Program Structure Interface and how to use it to obtain the content of the Java project that is currently open in IntelliJ. Once you complete the first step, you will understand how you can obtain the content of the loaded Java project, its modules, files, and declared types. Second, you create your own data structure for keeping tracks of the scopes where types are declared. Using this data structure (e.g., a hash table) allows you to quickly check if the newly generated class, for example, already exist and in what package/module/interface/class/method. Next, you will create an implementation of your design where you will integrate your name clash analyzer into the plugin and you may use a Java parser to construct an AST and to traverse it to determine if a leaf with a given name already exists. Fourth, you will create multiple unit tests using [JUnit framework](https://junit.org/junit5/) or some other framework like [Cucumber](https://cucumber.io/). Finally, you will install your plugin in IntelliJ, run tests, run your plugin, generate the messages to show name clashes with ones in some existing open Java project, and report the results. 

## Baseline
To be considered for grading, your project should include at least one of your own written programs in Java (i.e., not copied examples where you renamed variables or refactored them similarly), your project should be buildable using the SBT or the Gradle, and your documentation must specify your chosen abstractions with links to specific modules where they are realized. Your documentation must include your design and model, the reasoning about pros and cons, explanations of your implementation and the chosen design patterns that you used to implement DePaCoG, and the results of your runs. Simply copying some open-source Java programs from examples and modifying them a bit (e.g., rename some variables) will result in desk-rejecting your submission.

## Piazza collaboration
You can post questions and replies, statements, comments, discussion, etc. on Piazza. For this homework, feel free to share your ideas, mistakes, code fragments, commands from scripts, and some of your technical solutions with the rest of the class, and you can ask and advise others using Piazza on where resources and sample programs can be found on the internet, how to resolve dependencies and configuration issues. When posting question and answers on Piazza, please select the appropriate folder, i.e., hw1 to ensure that all discussion threads can be easily located. Active participants and problem solvers will receive bonuses from the big brother :-) who is watching your exchanges on Piazza (i.e., your class instructor and your TA). However, *you must not describe your design or specific details related how your construct your models!*

Since you can post your questions anonymously on pizza, there is no reason to be afraid to ask questions. Start working on this homework early and ask away! When you come to see your TA during his office hours don't ask him to debug your code - Mr.Siddiq **is prohibited** from doing so. You can discuss your design, abstractions, and show him your code to receive his feedback, but you should not ask him to solve problems for you!

## Git logistics
**This is an individual homework.** Separate repositories will be created for each of your homeworks and for the course project. You will fork the repository for this homework and your fork will be **private**, no one else besides you, the TA and your course instructor will have access to your fork. Please remember to grant a read access to your repository to your TA and your instructor. In future, for the team homeworks and the course project, you should grant the write access to your forkmates, but NOT for this homework. You can commit and push your code as many times as you want. Your code will not be visible and it should not be visible to other students (except for your forkmates for a team project, but not for this homework). When you push the code into the remote repo, your instructor and the TA will see your code in your separate private fork. Making your fork public or inviting other students to join your fork for an individual homework will result in losing your grade. For grading, only the latest push timed before the deadline will be considered. **If you push after the deadline, your grade for the homework will be zero**. For more information about using the Git and Bitbucket specifically, please use this [link as the starting point](https://confluence.atlassian.com/bitbucket/bitbucket-cloud-documentation-home-221448814.html). For those of you who struggle with the Git, I recommend a book by Ryan Hodson on Ry's Git Tutorial. The other book called Pro Git is written by Scott Chacon and Ben Straub and published by Apress and it is [freely available](https://git-scm.com/book/en/v2/). There are multiple videos on youtube that go into details of the Git organization and use.

Please follow this naming convention while submitting your work : "Firstname_Lastname_hw3" without quotes, where you specify your first and last names **exactly as you are registered with the University system**, so that we can easily recognize your submission. I repeat, make sure that you will give both your TA and the course instructor the read/write access to your *private forked repository* so that we can leave the file feedback.txt with the explanation of the grade assigned to your homework.

## Discussions and submission
As it is mentioned above, you can post questions and replies, statements, comments, discussion, etc. on Piazza. Remember that you cannot share your code and your solutions privately, but you can ask and advise others using Piazza and StackOverflow or some other developer networks where resources and sample programs can be found on the Internet, how to resolve dependencies and configuration issues. Yet, your implementation should be your own and you cannot share it. Alternatively, you cannot copy and paste someone else's implementation and put your name on it. Your submissions will be checked for plagiarism. **Copying code from your classmates or from some sites on the Internet will result in severe academic penalties up to the termination of your enrollment in the University**. When posting question and answers on Piazza, please select the appropriate folder, i.e., hw2 to ensure that all discussion threads can be easily located.


## Submission deadline and logistics
Monday, March 30 at 10PM CST via the bitbucket repository. Your submission will include the code for the program, your documentation with instructions and detailed explanations on how to assemble and deploy your program along with the results of your simulation and a document that explains these results based on the characteristics and the parameters of your models, and what the limitations of your implementation are. Again, do not forget, please make sure that you will give both your TAs and your instructor the read access to your private forked repository. Your name should be shown in your README.md file and other documents. Your code should compile and run from the command line using the commands **sbt clean compile test** and **sbt clean compile run** or the corresponding commands for Gradle. Also, you project should be IntelliJ friendly, i.e., your graders should be able to import your code into IntelliJ and run from there. Use .gitignore to exlude files that should not be pushed into the repo.


## Evaluation criteria
- the maximum grade for this homework is 8% with the bonus described above. Points are subtracted from this maximum grade: for example, saying that 2% is lost if some requirement is not completed means that the resulting grade will be 8%-2% => 6%; if the core homework functionality does not work, no bonus points will be given;
- only some POJO classes are created and nothing else is done: up to 7% lost;
- having less than five unit and/or integration tests: up to 5% lost;
- missing comments and explanations from the submitted program: up to 5% lost;
- logging is not used in your programs: up to 3% lost;
- hardcoding the input values in the source code instead of using the suggested configuration libraries: up to 4% lost;
- no instructions in README.md on how to install and run your program: up to 5% lost;
- the plugin crashes without completing the core functionality: up to 6% lost;
- no design and modeling documentation exists that explains your choices: up to 6% lost;
- the deployment documentation exists but it is insufficient to understand how you assembled and deployed all components of the program: up to 5% lost;
- the minimum grade for this homework cannot be less than zero.

Enjoy!!

## Installation and Execution

To download this program from bit-bucket, run this command in a git friendly command prompt:

`git clone https://MohamedSaeed99@bitbucket.org/MohamedSaeed99/mohamed_saeed_hw3.git` 

###### Using Intellij

Open the downloaded project in intellij and run it.

###### Using CommandLine

 1. Download and install Gradle, and make sure that you are able to use gradle via command line.

 2. Navigate to the project.

 3. Run `./gradlew build` to build the project.
 
 4. Run `./gradlew runIde` this will begin the execution of the program. 
