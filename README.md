# TooltoTest
This tool is for those who faces issues while debugging Android application.
If debugging is data driven then they have to provide backdoor.
Backdoor itself is a vulnerability which provide security loop holes in the applications.
Now to make our application debuggable without these loopholes plus with easily handling of debug parameters.
we need another approach.

What we can do to make sure that data which we want to inject in our application is genuine.
Digital Signature is the best option which satisfy this requirement.

Using this tool developers can digitally sign their files to be injected in their builds.
These digitally signed files can be can be place in pre-defined hierarchy specified below.

Now developer just have to use the SDOlib apis to access the files.
