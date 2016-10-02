
create table Groupe(
    id integer primary key autoincrement, 
    Nom varchar2(200) not null,
    description varchar2(1000),
    CONSTRAINT nom_unique UNIQUE (Nom )
);


create table niveau(
    id integer primary key autoincrement,
    Nom varchar2(200) not null,
    CONSTRAINT nom_unique UNIQUE (Nom )
);

create table capacite(
    id integer primary key autoincrement,
    Nom varchar2(200) not null,
    description varchar2(1000),
    CONSTRAINT nom_unique UNIQUE (Nom)
);


create table Aptitude(
    id integer primary key autoincrement,
    description varchar2(1000),
    niveau_id integer,
    capactite_id integer,    
    FOREIGN KEY(capactite_Id) REFERENCES Capacite(id),
    FOREIGN KEY(niveau_id) REFERENCES Niveau(id)
);


create table Eleve(
    id integer primary key autoincrement, 
    Nom varchar2(200)  not null,
    Prenom varchar2(200) not null,
    Groupe_Id integer ,
    capactite_Id integer ,
    FOREIGN KEY(Groupe_Id) REFERENCES Groupe(id),
    FOREIGN KEY(capactite_Id) REFERENCES Capacite(id)
);


insert into Groupe(nom,description ) values ("A","Petit bassin - Petite profondeur");
insert into Groupe(nom,description ) values ("B","Petit bassin - Moyenne profondeur");
insert into Groupe(nom,description ) values ("C","Grand bassin - Moyenne profondeur");
insert into Groupe(nom,description ) values ("D","Grand bassin - Grande profondeur");

insert into niveau (nom) values (1);
insert into niveau (nom) values (2);
insert into niveau (nom) values (3);
insert into niveau (nom) values (4);


--Remplissage d'exemple pour démo
insert into capacite(nom ) values ("Etoile de mer");
insert into capacite(nom ) values ("Coquillage");
insert into capacite(nom ) values ("Canard");
insert into capacite(nom ) values ("Crocodile");
insert into capacite(nom ) values ("Poisson");
insert into capacite(nom ) values ("Tétard");
insert into capacite(nom ) values ("Triton");
insert into capacite(nom ) values ("Dauphin");
insert into capacite(nom ) values ("Requin");

insert into Eleve(prenom,nom, groupe_id) values ("mylene", "farmer" , 1);
insert into Eleve(prenom,nom, groupe_id) values ("michael", "jackson" , 1);
insert into Eleve(prenom,nom, groupe_id) values ("Nicola", "Sirkis" , 1);
insert into Eleve(prenom,nom, groupe_id) values ("Claude", "Nougaro" , 2);
insert into Eleve(prenom,nom, groupe_id) values ("Celine", "Dion" , 3);
insert into Eleve(prenom,nom, groupe_id) values ("Gérard", "Lenormand" , 3);
insert into Eleve(prenom,nom, groupe_id) values ("Justin bieber", "Lenormand" , 1);
        