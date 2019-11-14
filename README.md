# SYMlab2

## 4.1 Traitement des erreurs
*<u>Les classes et interfaces SymComManager et CommunicationEventListener utilisées au point 3.1
restent très (et certainement trop) simples pour être utilisables dans une vraie application : que se
passe-t-il si le serveur n’est pas joignable dans l’immédiat ou s’il retourne un code HTTP d’erreur ?
Veuillez proposer une nouvelle version, mieux adaptée, de ces deux classes / interfaces pour vous aider
à illustrer votre réponse.</u>*

Si un code d'erreur est généré dans l'état actuel de l'application, l'application continuera sont exécution sans qu'aucune erreur n'ait été générée. Concrètement si une erreur est générée, elle sera affichée comme si le message d'erreur est la réponse que l'on attendait de la part du serveur. Ce message d'erreur sera donc affiché dans la réponse et la requête sera considérée comme traitée.

Le minimum à faire pour géré au moins le cas ou le serveur n'est pas joignable pour une quelconque raison est de vérifier si une erreur est survenue. Le but est donc de tester dans le code si la réponse retournée contient bien le code 200, qui correspond à HTTP_OK (réponse du serveur qui annonce que tout s'est bien passé).
Il existe bien d'autres erreurs (401 : utilisateur non authentifil, 403 : accès refusé, 404 : page non trouvée) qu'il serait intéressant de vérifier pour donner les informations les plus précises à l'utilisateur et que celui-ci comprenne ce qui n'a pas fonctionné et ce qu'il peut éventuellement faite pour remédier au problème.

## 4.2 Authentification
*<u>Si une authentification par le serveur est requise, peut-on utiliser un protocole asynchrone ? Quelles
seraient les restrictions ? Peut-on utiliser une transmission différée ?</u>*

Une authentification par le serveur ne pose aucun problème dans le cas d'une communication asynchrone. En effet le serveur peut très bien exiger du client une authentification. Le serveur va donc demander au client d'envoyer dans chaque requête un token d'identification pour traiter la requête si la session du client est toujours valide. Les échanges d'informations sont indispensables dans l'autre sens, ainsi, si le serveur valide l'authentification du client, il lui transmettra via la réponse les informations relatives à l'authentification.

Cela implique de devoir ajouter un champs en plus pour renseigner l'authentification. Il faut donc un traitement en plus de la part du serveur et il faut que le client traite aussi l'authentification correctement. En général cela passe par la génération d'un ID unique de la part du serveur mais il faut malgré tout des étapes supplémentaires de traitement.

Enfin pour le cas d'une transmission différée, il est compliqué de gérer le cas d'une authentification. Cela requiert une communication directe avec le serveur au moment ou la demande est envoyée. La transmission différée implique que la connexion avec le serveur n'est pas toujours disponible, ainsi les échanges relatifs à l'authentification ne pourront peut-être pas être effectués.

## 4.3 Threads concurrents
*<u>Lors de l'utilisation de protocoles asynchrones, c'est généralement deux threads différents qui se
préoccupent de la préparation, de l'envoi, de la réception et du traitement des données. Quels
problèmes cela peut-il poser ?</u>*

Il peut y avoir plusieurs problèmes qui seront tous liés à la coordination des threads.
Par exemple un thread qui envoie trop de requêtes peut remplir le buffer de celui qui reçoit (qui est peut-être actuellement en train de traiter autre chose). Le thread qui envoie continue d'envoyer des requêtes mais elles seront ignorées par le thread qui reçoit. Malgré tout la coordination passe par les réponses aussi, si le thread qui envoie une requête ne reçoit pas de réponse il doit considérer qu'elle n'a pas été traitée.

Il faut toujours veiller à ce que les étapes soient effectuées dans le bon ordre, notamment dans le cas d'une authentification. Le client doit bien attendre la réponse du serveur qui valide bien son athentification avant d'envoyer la requête.

Comme toujours lors des programmes asynchrones, il est possible que le thread qui traite les données soit en train de traiter des données et que ce traitement soit plus long que le temps pour envoyer les données. Ainsi s'il y a un envoi constant de requêtes, la reception sera tardive et si le client a un timeout concernant le retour de la réponse (et que ce timeout est passé), il va (suivant l'implémentation) renvoyer la requête et celle-ci sera traitée deux fois.

Enfin il peut aussi y avoir un problème concernant l'accès aux sections critiques comme dans tout programme concurrentiel. Il faut faire attention à bien protéger les variables lorsque c'est nécessaire pour éviter des problèmes (mais en général ce n'est pas vraiment problématique dans la gestion).


## 4.4 Ecriture différée
*<u>Lorsque l'on implémente l'écriture différée, il arrive que l'on ait soudainement plusieurs transmissions
en attente qui deviennent possibles simultanément. Comment implémenter proprement cette
situation (sans réalisation pratique) ? Voici deux possibilités :
• Effectuer une connexion par transmission différée
• Multiplexer toutes les connexions vers un même serveur en une seule connexion de transport.
Dans ce dernier cas, comment implémenter le protocole applicatif, quels avantages peut-on
espérer de ce multiplexage, et surtout, comment doit-on planifier les réponses du serveur
lorsque ces dernières s'avèrent nécessaires ?
Comparer les deux techniques (et éventuellement d'autres que vous pourriez imaginer) et discuter des
avantages et inconvénients respectifs.</u>*

<u>Connexion par transmission différée (ce que l'on fait dans le laboratoire).</u>

Avantages : 
- Lorsqu'un problème de connexion avec le serveur est recontré, seul les paquets non reçus seront perdus, les autres seront traités
- On revoie les réponses au fur et a mesure, le client peut traiter les réponses petit à petit.
- Implémentation assez simple
- 

Inconvénients : 
- On ne peut pas garantir l'ordre d'arrivée des requêtes
- Si une authentification est demandée par le serveur cela n'est pas vraiment adapté (comme expliqué plus haut).
- Peut poser problème si le serveur doit gérer un très grand nombre de requêtes

<u>On appelle multiplexage, la capacité à transmettre sur un seul support physique (appelé voie haute vitesse), des données provenant de plusieurs paires d'équipements (émetteurs et récepteurs) ; on parle alors de voies basse vitesse. </u>

Avantages : 
- Très utile pour le cas où l'on veut envoyer beaucoup de tâches courtes 
- Permet comme dit dans l'explication du multiplexage ci-dessus de transférer plusieurs requêtes d'un seul coup. Cela implique une seule connexion pour la file d'attente. 
- Le traitement des requêtes sera fait dans l'ordre d'arrivée.
- Fiablilité des données plus grande

Inconvénients : 
- Compliqué car les requêtes vont être fusionnées 
- Pose problème si la taille des paquets est excessive
- Peut poser des problèmes si la connexion est régulièrement coupée (ou si c'est lent, car l'emetteur des paquets va attendre longtemps avant de recevoir quelque chose puisqu'une seule connexion est ouverte pour tout traiter) 
- En cas d'erreurs renvoie toutes les requêtes ce qui peut être lent.

## 4.5 Transmission d’objets
*<u>a. Quel inconvénient y a-t-il à utiliser une infrastructure de type REST/JSON n'offrant aucun
service de validation (DTD, XML-schéma, WSDL) par rapport à une infrastructure comme SOAP
offrant ces possibilités ? Est-ce qu’il y a en revanche des avantages que vous pouvez citer ?</u>*

Inconvénient : On peut recevoir des données malformées avec des champs manquants ou mal renseignés. Le format pourrait ne pas être le bon etc... Il faut pour palier à cela que le serveur qui reçoit ces information détecte d'une façon où d'une autre que les données sont fausses, ou bien il prend le parti de traiter les informations et de renvoyer quelque chose de faux, ou encore il renvoie le message d'erreur qu'aura généré le traitement de mauvaises informations.

Avantage : exécution plus rapide (car pas de temps d'exécution lié à la vérification), et implémentation plus aisée. Plus flexible car on pourra avoir des contenus ne correspondant exactement pas à ce qui est spécifié dans la validation. Cela permettrait d'avoir de l'information supplémentaire inutile dans le cas de l'application mais qui ne serait pas rejeté à cause de la validation.

*<u>b. L’utilisation d’un mécanisme comme Protocol Buffers9 est-elle compatible avec une
architecture basée sur HTTP ? Veuillez discuter des éventuelles avantages ou limitations par
rapport à un protocole basé sur JSON ou XML ?</u>*

C'est un moyen de sérialisation des données compatible avec une architecture basée sur HTTP

La conception du Protocol Buffers avait pour objectif la simplicité et la performance avec en-tête d’être plus léger et plus rapide que le XML. 
Les Protocol Buffers sont très similaires au protocole Apache Thrift (utilisé par Facebook par exemple), sauf que l’implémentation publique du Protocole Buffers ne comprend pas de véritable ensemble de protocoles RPC voué à des services spécifiques. 
Les messages sont sérialisés sous un format binaire compact, donc contrairement à du JSON ou du XML qui restent lisible pour nous, il est impossible de comprendre un format binaire.

*<u>Par rapport à l’API GraphQL mise à disposition pour ce laboratoire. Avez-vous constaté des
points qui pourraient être améliorés pour une utilisation mobile ? Veuillez en discuter, vous
pouvez élargir votre réflexion à une problématique plus large que la manipulation effectuée.</u>*

Lorsque l'on fait une requête on doit attendre d'avoir toutes les données pour pouvoir les affichées dans notre programme. Des requêtes qui permettraient l'aquisition d'une plus petite partie des données (pagination) pourrait être intéressante et rendrait l'application plus réactive.
Aussi si les champs de la base de données venaient à changer, nous serions dans l'incapacité de pouvoir intéragir avec celle-ci. Un système de d'update des requête utilisables ou une utilisation de requêtes GET pourrait palier à ce soucis.


## 4.6 Transmission compressée
*<u>Quel gain peut-on constater en moyenne sur des fichiers texte (xml et json sont aussi du texte) en
utilisant de la compression du point 3.4 ? Vous comparerez plusieurs tailles et types de contenu.</u>*





