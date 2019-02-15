# Jbody ![1](https://travis-ci.org/Ruslan08/Jbody.svg?branch=master)

N-body algorithm is taken from research article [Performance Analysis of Direct N-Body Algorithms on Special-Purpose Supercomputers](https://arxiv.org/abs/astro-ph/0608125)

Integration consists of the following steps: 

**(1) The initial time steps are calculated from**

![equation](http://latex.codecogs.com/gif.latex?%5CDelta%20t_%7Bi%7D%20%3D%20%5Ceta%20_%7Bs%7D%5Cfrac%7B%5Cmid%20a_%7Bi%7D%5Cmid%7D%7B%5Cmid%20%5Cdot%7Ba%7D_%7Bi%7D%5Cmid%7D%2C)

where typically ηs = 0.01 gives sufficient accuracy.

**(2) The system time** *t* is set to the minimum of all ![eq](http://latex.codecogs.com/gif.latex?t_%7Bi%7D%20&plus;%20%5CDelta%20t_%7Bi%7D) and all particles *i*
that have ![eq](http://latex.codecogs.com/gif.latex?t_%7Bi%7D%20&plus;%20%5CDelta%20t_%7Bi%7D%20%3D%20t) are selected as active particles. Note that “classical”
N-body codes (Aarseth 1999, 2003) employ a sorted time-step list to
select short time step particles efficiently. Such sorting is abandoned in
favor of a search over all N particles, which improves load balance in the
parallel code due to the more random distribution of short and long step
particles.

**(3) Positions and velocities at the new t are predicted for all particles using**

![eq](http://latex.codecogs.com/gif.latex?x_%7Bj%2Cp%7D%20%3D%20x_%7Bj%2C0%7D%20&plus;%20%28t-t_%7Bj%7D%29v_%7Bj%2C0%7D%20&plus;%20%5Cfrac%7B%28t-t_%7Bj%7D%29%5E%7B2%7D%7D%7B2%7Da_%7Bj%2C0%7D%20&plus;%20%5Cfrac%7B%28t-t_%7Bj%7D%29%5E%7B3%7D%7D%7B6%7D%5Cdot%7Ba%7D_%7Bj%2C0%7D)

and

![eq](http://latex.codecogs.com/gif.latex?v_%7Bj%2Cp%7D%20%3D%20v_%7Bj%2C0%7D%20&plus;%20%28t-t_%7Bj%7D%29a_%7Bj%2C0%7D%20&plus;%20%5Cfrac%7B%28t-t_%7Bj%7D%29%5E%7B2%7D%7D%7B2%7D%5Cdot%7Ba%7D_%7Bj%2C0%7D)

Here, the second subscript denotes a value given either at the beginning
(0) or the end (1) of the current time step. All quantities used in the
predictor can be calculated directly, i.e. no memory of a previous time
step is required.

**(4) Acceleration and its time derivative are updated for active particles only according to**

![eq](http://latex.codecogs.com/gif.latex?a_%7Bi%2C1%7D%20%3D%20%5Csum_%7Bj%5Cneq%20i%7DGm_%7Bj%7D%5Cfrac%7Br_%7Bij%7D%7D%7B%28r%5E%7B2%7D_%7Bij%7D%20&plus;%20%5Cepsilon%5E%7B2%7D%29%5E%7B%283/2%29%7D%7D)

and

![eq](http://latex.codecogs.com/gif.latex?%5Cdot%7Ba%7D_%7Bi%2C1%7D%20%3D%20%5Csum_%7Bj%5Cneq%20i%7DGm_%7Bj%7D%5Cleft%20%5B%20%5Cfrac%7Bv_%7Bij%7D%7D%7B%28r%5E%7B2%7D_%7Bij%7D%20&plus;%20%5Cepsilon%5E%7B2%7D%29%5E%7B%283/2%29%7D%7D%20&plus;%20%5Cfrac%7B3%28v_%7Bij%7D%20%5Ccdot%20r_%7Bij%7D%29r_%7Bij%7D%7D%7B%28r%5E%7B2%7D_%7Bij%7D%20&plus;%20%5Cepsilon%5E%7B2%7D%29%5E%7B%285/2%29%7D%7D%20%5Cright%20%5D)

where

![eq](http://latex.codecogs.com/gif.latex?r_%7Bij%7D%20%3D%20x_%7Bj%2Cp%7D%20-%20x_%7Bi%2Cp%7D)

![eq](http://latex.codecogs.com/gif.latex?v_%7Bij%7D%20%3D%20v_%7Bj%2Cp%7D%20-%20v_%7Bi%2Cp%7D)

and ![eq](http://latex.codecogs.com/gif.latex?%5Cepsilon) is the softening parameter.

**(5) Positions and velocities of active particles are corrected using**

![eq](http://latex.codecogs.com/gif.latex?x_%7Bi%2C1%7D%20%3D%20x_%7Bi%2Cp%7D%20&plus;%20%5Cfrac%7B%5CDelta%20t_%7Bi%7D%5E4%7D%7B24%7Da_%7Bi%2C0%7D%5E%7B%282%29%7D%20&plus;%20%5Cfrac%7B%5CDelta%20t_%7Bi%7D%5E5%7D%7B120%7Da_%7Bi%2C0%7D%5E%7B%283%29%7D)

and

![eq](http://latex.codecogs.com/gif.latex?v_%7Bi%2C1%7D%20%3D%20v_%7Bi%2Cp%7D%20&plus;%20%5Cfrac%7B%5CDelta%20t_%7Bi%7D%5E3%7D%7B6%7Da_%7Bi%2C0%7D%5E%7B%282%29%7D%20&plus;%20%5Cfrac%7B%5CDelta%20t_%7Bi%7D%5E4%7D%7B24%7Da_%7Bi%2C0%7D%5E%7B%283%29%7D)

where the second and third time derivatives of a are given by

![eq](http://latex.codecogs.com/gif.latex?a_%7Bi%2C0%7D%5E%7B%282%29%7D%20%3D%20%5Cfrac%7B-6%28a_%7Bi%2C0%7D-a_%7Bi%2C1%7D%29%20-%20%5CDelta%20t_%7Bi%7D%284%20%5Cdot%7Ba%7D_%7Bi%2C0%7D&plus;2%5Cdot%7Ba%7D_%7Bi%2C1%7D%29%7D%7B%5CDelta%20t_%7Bi%7D%5E2%7D)

![eq](http://latex.codecogs.com/gif.latex?a_%7Bi%2C0%7D%5E%7B%283%29%7D%20%3D%20%5Cfrac%7B12%28a_%7Bi%2C0%7D-a_%7Bi%2C1%7D%29%20&plus;%206%5CDelta%20t_%7Bi%7D%28%5Cdot%7Ba%7D_%7Bi%2C0%7D&plus;%5Cdot%7Ba%7D_%7Bi%2C1%7D%29%7D%7B%5CDelta%20t_%7Bi%7D%5E3%7D)

**(6) The times ti are updated and the new time steps ∆ti are determined.**

Time steps are calculated using the standard formula (Aarseth 1985):

![eq](http://latex.codecogs.com/gif.latex?%5CDelta%20t_%7Bi%2C1%7D%20%3D%20%5Csqrt%7B%5Ceta%20%5Cfrac%7B%5Cleft%20%7C%20a_%7Bi%2C1%7D%20%5Cright%20%7C%5Cleft%20%7C%20a_%7Bi%2C1%7D%5E%7B%282%29%7D%20%5Cright%20%7C%20&plus;%20%5Cleft%20%7C%20%5Cdot%7Ba%7D_%7Bi%2C1%7D%20%5Cright%20%7C%5E2%7D%7B%5Cleft%20%7C%20%5Cdot%7Ba%7D_%7Bi%2C1%7D%20%5Cright%20%7C%5Cleft%20%7C%20a_%7Bi%2C1%7D%5E%7B%283%29%7D%20%5Cright%20%7C%20&plus;%20%5Cleft%20%7C%20a_%7Bi%2C1%7D%5E%7B%282%29%7D%20%5Cright%20%7C%5E%7B2%7D%7D%7D)

The parameter ![eta](http://latex.codecogs.com/gif.latex?%5Ceta) controls the accuracy of the integration and is typically
set to 0.02. The value of ![a](http://latex.codecogs.com/gif.latex?a_%7Bi%2C1%7D%5E%7B%282%29%7D) is calculated from

![eq](http://latex.codecogs.com/gif.latex?a_%7Bi%2C1%7D%5E%7B%282%29%7D%20%3D%20a_%7Bi%2C0%7D%5E%7B%282%29%7D%20&plus;%20%5CDelta%20t_%7Bi%2C0%7Da_%7Bi%2C0%7D%5E%7B%283%29%7D)

and ![a](http://latex.codecogs.com/gif.latex?a_%7Bi%2C1%7D%5E%7B%283%29%7D) is set to ![eq](http://latex.codecogs.com/gif.latex?a_%7Bi%2C0%7D%5E%7B%283%29%7D).

**(7) Repeat from step (2).**
