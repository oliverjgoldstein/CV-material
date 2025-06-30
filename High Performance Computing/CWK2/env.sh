
module load languages/python-2.7.6
module load languages/intel-compiler-16-u2
module load openmpi/intel/64/1.6.5
module load languages/intel-compiler-15

export MPICH_RANK_REORDER_METHOD=1
export KMP_AFFINITY=noverbose,granularity=fine,compact
export PHI_KMP_PLACE_THREADS=16c,1t
export KMP_BLOCKTIME=150