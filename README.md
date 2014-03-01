collectivesim
=============

A java framework for agent based simulation that reifies design patterns like observe, visitor and factory to ease the development of models. It is strongly influenced by Ascape (http://ascape.sourceforge.net) 

The main concepts for collective are:
* Agent: is an entity that expose behaviors (methods) and attributes
* Model: is a collection of agents and their associated collective behaviors and state
* Factory: is a generator of agents to populate a model
* Behavior: is the invocation of a behaviors on the agents of a model. Can be schedulled at specific times and be repeted periodically
* Observer: is an inspector of the state of the agents of a model. 
* Sampler: allows the selection of a subset of the agents of a model to be considered by Behaviors and Observers
* Dataseries: is a series of tuples produced by  observing a model
* State: is the aggregation of the states of agents using a summarization function like count, sum, max, etc.
* Stream: is a series of values that can be used to feed a model. Are used as input to behaviors and factories. Can be a fixed value, a series of random values, read from a file, etc.
* Experiment: is the setup of all the elements of a simulation, the paremeterization of its execution and the collection of results

TODO
* Create getting started guide
* create examples
* unit tests
