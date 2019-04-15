


                    README Hospital-Urgency-Pipeline



            Proiectul a fost gandit in felul urmator:

         --> Parsarea a fost facuta manual cu JsonNode tot din Jackson
                --> Se parsa o singura data tot arborele, apoi se accesa fiecare camp din fisierul json

         --> Pacientii au fost simulati cu ajutorul clasei Patient cu campurile urmatoare:

                --> InvestigationResult investigationResult
                        --> verdictul dat de ER_Technician
                --> int id --> id-ul din fisierul de intrare
                --> String name --> numele pacientului
                --> int remainingRounds --> numarul de runde cat trebuie sa mai stea in spital
                --> Urgency urgency --> gradul de urgenta
                --> State --> reprezinta stadiul actual al pacientului (ex: HOME_SURGEON, EXAMINATIONS_QUEUE)

                --> Pentru crearea pacientilor am folosit clasa PacientFactory

         --> Doctorii au fost simulati cu ajutorul clasei abstracte Doctor, pe care am extins-o in clasele:

                --> Cardiologist
                --> ERPhysician
                --> Gastroenterologist
                --> GeneralSurgeon
                --> Internist
                --> Neurologist

                --> Fiecare subclasa are parametrii c1, c2, precum si isSurgeon diferiti

                --> Pentru crearea doctorilor am folosit clasa DoctorFactory

                --> Doctorii decid daca un pacient poate fi trimis acasa, daca trebuie trimis la investigatii

                --> Tot acestia opereaza si spitalizeaza pacientul ( metodele operate si hospitalize ), in functie de
                        severitatea afectiunii pacientului si de c1 si c2

                --> Cand efectueaza o actiune asupra unui pacient, doctorii ii modifica state-ul.


         --> Fiecare doctor are un EnumSet affections unde se gasesc afectiunile pe
                care doctorul respectiv le poate trata.

         --> Listele cu doctori au fost gandite ca un HashMap< IllnessType, List<Doctor>>, unde practic
                retineam liste cu doctori in functie de diferite afectiuni. Cu ajutorul functiilor addInLists
                si removeFromLists din clasa Doctor, adaugam si eliminam doctori din colectia de liste,
                conform enuntului.

         --> Asistentele sunt reprezentate de clasa Nurse

                    --> prin metoda consult, acestea asigneaza un nivel de urgenta unui pacient si il adauga in
                            ExaminationsQueue

                    --> prin metoda treat trateaza pacientul, adica ii scad T = 22 din severitate si ii decrementeaza
                            numarul de runde ramase

                    --> prin metoda update suntem notificati de catre pacienti. Asistentele verifica daca pacientul este
                            la tratament, caz in care ii aplica tratamentul si salveaza in nurseLog din MedicalDB
                            faptul ca l-au tratat (ex: "Nurse 740 treated West Scott and patient has 9 more rounds")

         --> Toate datele necesare sunt centralizate in clasa MedicalDB, implementata cu Singleton
                --> In aceasta clasa se gasesc lista initiala cu pacienti, cea cu asistente, listele cu doctori,
                    precum si cele 3 "cozi" necesare, respectiv triageList, examinationList si investigationsList


         Logica Observer - Observable:

                --> Observer sunt doctorii si asistentele

                --> Observable sunt pacientii

                --> Atunci cand un pacient este consultat prima oara de catre o asistenta, adaugam in
                       lista sa de observeri asistenta cu indexul 0 din MedicalDB deoarecem folosim pattern-ul
                       Chain Of Responsibility

                --> Atunci cand un pacient trebuie sa fie tratat de o asistenta, apelam update si vom fi directionati
                        catre prima asistenta din sirul cu asistente. Daca aceasta nu este disponibila vom fi
                        redirectionati catre asistenta 1, apoi catre asistenta 2 samd pana cand gasim o asistenta
                        disponibila

                --> Daca luam toate asistentele ca Observeri, ar fi fost ineficient dpdv al memoriei si al timpului
                        de rulare

                --> Cand un doctor interneaza un pacient, doctorul este adaugat in lista de observeri a pacientului.
                        Doctorul vegheaza asupra starii pacientului si decide in fiecare runda daca acesta poate
                        pleca acasa sau nu.


         Actiunea Simularii:

                --> Inainte de inceperea rundelor, cream listele cu doctori, in functie de
                        afectiuni, cu doctorii in ordinea in care se gasesc in fisierul de
                        intrare

                --> In fiecare runda a simularii trecem prin cele 3 etape Triage, Examinations si Investigations

                --> TriageQueue

                        --> coada concreta se gaseste in triageList din MedicalDB si reprezinta un PriorityQueue de
                                pacienti cu un comparator ce ordoneaza pacienti dupa severitate si apoi dupa nume.

                        --> In triageList adaugam pacientii care sosesc in runda curenta
                        --> Consultam pacientii pentru a le stabili urgenta in functie de cate asistente avem.
                                Deci daca avem 4 asistente, vom consulta maxim 4 pacienti. Pacientii neconsultati
                                raman in coada pentru rundele urmatoare

                        --> Dupa ce consultam un pacient, ii adaugam ca Observer asistenta cu indexul 0 din MedicalDB,
                                pentru a ne folosi apoi de design pattern-ul Chain of Responsibility atunci cand tratam
                                pacientii.

                --> ExaminationsQueue

                        --> coada concreta se gaseste in examinationList din MedicalDB si reprezinta un PriorityQueue de
                                pacientii cu comparatorul patientComparator din clasa Patient, ce compara pacientii in functie
                                de Urgency, severitate si nume.

                        --> In Examinations Queue extragem pacienti din lista pana cand aceasta se goleste.

                        --> Cand extragem un pacient, cautam in lista aferenta afectiunii acestuia un doctor.

                        --> Daca nu gasim niciun doctor, atunci trimitem pacientul la un alt spital

                        --> Daca gasim un doctor disponibil, atunci cu ajutorul metodelor removeFromLists si addInLists il scoatem
                                si il adaugam inapoi in listele in care se gaseste

                        --> Cand selectam un doctor, avem grija sa verificam daca acesta este chirurg in cazul in care pacientul
                                nostru necesita sa fie operat.

                        --> Cand doctorii consulta un pacient, verifica daca severitatea acestuia < maxForTreatment si atunci
                                decid daca il trimit acasa cu tratament sau mai departe in InvestigationsQueue, sau daca
                                pacientul a fost deja vazut de un ER_Technician, doctorul va aplica sfatul acestuia

                --> InvestigationsQueue

                        --> coada concreta este identica cu examinationsList

                        --> se extrag pacienti din investigationsList in functie de cati ER_Technicians avem

                        --> ER_Technicians consulta pacientii si dau un verdict pe baza severitatii, conform enuntului

                        --> toti pacientii vazuti de un ER_Technician ajung inapoi in ExaminationsQueue


         Afisarea:

                --> Pentru afisare avem 2 log-uri in clasa MedicalDB, unul pentru actiunile asistentelor (nurseLog),
                        si unul pentru actiunile doctorilor (doctorLog)

                --> nurseLog
                            --> un string pe care il golim la inceputul fiecarei runde

                --> doctorLog
                            --> Map de tipul TreeMap<Integer, List<String>> in care tinem ordonate mesajele de la
                                    fiecare doctor in functie de un id ce reprezinta ordinea doctorului din fisierul
                                    de intrare

                            --> Am recurs la aceasta solutie pentru a afisa mesajele doctorilor in ordinea in care
                                    se gasesc acestia in fisierul de intrare

                --> Pentru starea pacientilor ce au ajuns in spital, ne folosim de campul state

                --> Practic luam toti pacientii in ordine alfabetica si afisam valoarea lui State din enum-ul State,
                        care este un string

