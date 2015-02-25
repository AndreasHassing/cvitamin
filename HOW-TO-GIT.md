Git Introduktion for SWU Studerende
===================================
> En kort introduktion til hvordan man **kan** bruge Git til projektarbejde.

Branches
--------
En branch er en forgrening af projektet, dvs. at når du brancher ud fra master, tager den en kopi af arbejdet der er lavet op til det punkt, og laver en gren som du kan arbejde videre fra, uden at arbejde direkte i `master`.

--------

Forestil dig at vi skal lave et program der skal kunne bage en kage.

Jeg skal eksempelvis tilføje en funktionalitet til projektet, som kan tilføje gær til dejen.

```bash
$ git checkout -b feature/add_yeast_to_dough
Switched to a new branch 'feature/add_yeast_to_dough'
# Nu er vi i vores nye branch!
# Lad os sige at vi nu har lavet funktionaliteten i CakeBakery.java
$ git status
On branch feature/add_yeast_to_dough
Untracked files:
  (use "git add <file>..." to include in what will be committed)

	CakeBakery.java
# Lad os sætte CakeBakery til staging (klar til commit)
$ git add CakeBakery.java
$ git commit -m "add yeast to dough functionality"
[feature/add_yeast_to_dough aba90b2] add yeast to dough funcitonality
 1 file changed, 1 insertion(+)
 create mode 100644 CakeBakery.java
# Tid til at pushe det vores branch til git!
$ git push -u origin feature/add_yeast_to_dough
```

Efterfølgende kan der laves et PR (pull request) på branchen igennem github.

Når den nye feature er lagt ud som PR, skal et andet gruppemedlem kigge koden igennem før ændringerne trækkes helt ind i `master`.

---------
> Andreas Bjørn Hassing Nielsen :balloon:
