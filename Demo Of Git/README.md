How to send a Pull Request(PR) to GitHub Project ?

Make sure git is install before following these commands. Just type git in your terminal/cmd and then you are good to go.

     1. Fork the project. Click on the fork button on the top right corner.
     2. Clone the repository using the command,
          git clone https://github.com/mohan08p/NTALMiniProject
        The username i.e. mohan08p must be replaced with yout username on Github(GitHub handler).
     3. Go to the respective directory i.e. NTALMiniProject
        Create a new Branch using the command
          git checkout -b branch_name
        (you can check the status using *git status* command)
     4. Create new directory where you can put your code, doc, ppt, etc.
     5  Add those changes i.e. tracked by git with the command
          git add .
     6. Then, commit so the change is been tracked using the command
          git commit -m "some message"
          (you can check the status using *git status* command)
     7. Then push your code to your fork repository using command,
          git push --set-upstream origin branch-name
     8. Go to github repository(your repository) and send a Pull Request(PR).