#!/bin/sh
: "${HPC_SSH_TARGET:?Set HPC_SSH_TARGET to user@host}"
ssh -X "$HPC_SSH_TARGET"
