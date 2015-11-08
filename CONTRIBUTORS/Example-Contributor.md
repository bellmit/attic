This is an example contributor agreement. To sign the agreement:

1. **Read it** and make sure you understand it.

2. Copy the agreement:

        cp CONTRIBUTORS/Example-Contributor.md CONTRIBUTORS/My-Name.md

3. Fix up the `BLANKS`:

        # or use emacs/vim/textmate/sublime/et cetera
        sed -i '' \
            -e 's,FULLNAME,My Name,g' \
            -e 's,GITHUBNAME,my-github-username,g' \
            -e 's,YEAR,2015,g' \
            CONTRIBUTORS/My-Name.md

4. Remove this preamble.

5. Optionally, sign the agreement with your GPG public key:

        gpg --armor --detach-sign CONTRIBUTORS/My-Name.md

6. Submit the new agreement (and your GPG signature, if any) in a pull request:

        git add CONTRIBUTORS/My-Name.md
        # git add CONTRIBUTORS/My-Name.md.asc
        git commit -m 'Contributor agreement for Ny Name.'
        git push my-remote my-branch
        hub pull-request

If you do not have a GPG key, you can generate one using Gnu Privacy Guard, or via https://keybase.io/. A Keybase proof associating the key with your Github account will be helpful, but is not required.

Remove all of the above, this line, and the following line.
-----
# Unacceptable Contributor Agreement

I, FULLNAME, also known as GITHUBNAME, agree that my contributions to the "unacceptable" project may be distributed under the MIT licence, as follows:

>The MIT License (MIT)
>
>Contributions copyright (c) YEAR FULLNAME
>
>Permission is hereby granted, free of charge, to any person obtaining a copy
>of this software and associated documentation files (the "Software"), to deal
>in the Software without restriction, including without limitation the rights
>to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
>copies of the Software, and to permit persons to whom the Software is
>furnished to do so, subject to the following conditions:
>
>The above copyright notice and this permission notice shall be included in
>all copies or substantial portions of the Software.
>
>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
>IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
>FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
>AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
>LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
>OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
>THE SOFTWARE.
