# UCLA-Linguistics-Parse-and-Practice-Program
A software designed for the syntax part for Ling20:Introductory Linguistics at UCLA

linguistics students can use this software to practice syntax parsing 
linguistics teachers can use this software to design student practice sets
syntacticians can use this software to draw parsing trees conveniently

The source code is able to generate two versions of the software:
teacherMode and studentMode.

The following piece of code inside src/program/Total.java controls this.
Total.java is the main entrance for this software.

		treeMode.practiceMode();
		menu.studentmode();

		//treeMode.teacherMode();
		//menu.teachermode();


This is an Eclipse project, you can clone it and directly import it into Eclipse workspace.

Under /resource, there are two sample txt files for homework assignment and grammar file.

Under /version, there is a student.jar and teacher.jar ready for use

Under /inProgressSource, there is an developing copy of the /src. It does not compile for now but it has better structure and shorter length of each file for easier understanding. If you are interested in continuing to develop this software, take a look in this folder and start from there!

Feel free to email me about your questions of the source code at ajwalburg@gmailcom. 






