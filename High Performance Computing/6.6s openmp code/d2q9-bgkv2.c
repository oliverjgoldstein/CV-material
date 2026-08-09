/*
** Code to implement a d2q9-bgk lattice boltzmann scheme.
** 'd2' inidates a 2-dimensional grid, and
** 'q9' indicates 9 velocities per grid cell.
** 'bgk' refers to the Bhatnagar-Gross-Krook collision step.
**
** The 'speeds' in each cell are numbered as follows:
**
** 6 2 5
**  \|/
** 3-0-1
**  /|\
** 7 4 8
**
** A 2D grid:
**
**           cols
**       --- --- ---
**      | D | E | F |
** rows  --- --- ---
**      | A | B | C |
**       --- --- ---
**
** 'unwrapped' in row major order to give a 1D array:
**
**  --- --- --- --- --- ---
** | A | B | C | D | E | F |
**  --- --- --- --- --- ---
**
** Grid indicies are:
**
**          ny
**          ^       cols(jj)
**          |  ----- ----- -----
**          | | ... | ... | etc |
**          |  ----- ----- -----
** rows(ii) | | 1,0 | 1,1 | 1,2 |
**          |  ----- ----- -----
**          | | 0,0 | 0,1 | 0,2 |
**          |  ----- ----- -----
**          ----------------------> nx
**
** Note the names of the input parameter and obstacle files
** are passed on the command line, e.g.:
**
**   d2q9-bgk.exe input.params obstacles.dat
**
** Be sure to adjust the grid dimensions in the parameter file
** if you choose a different obstacle file.
*/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#include <sys/time.h>
#include <sys/resource.h>
#include <omp.h>

#define NSPEEDS         9
#define FINALSTATEFILE  "final_state.dat"
#define AVVELSFILE      "av_vels.dat"
#define NUM_THREADS     16

// #define NUM

/* struct to hold the parameter values */
typedef struct {

    int    nx;            /* no. of cells in x-direction */
    int    ny;            /* no. of cells in y-direction */
    int    maxIters;      /* no. of iterations */
    int    reynolds_dim;  /* dimension for Reynolds number */
    float  density;       /* density per link */
    float  accel;         /* density redistribution */
    float  omega;         /* relaxation parameter */
} t_param;

/* struct to hold the 'speed' values */
typedef struct {
    float *north;
    float *south;
    float *east;
    float *west;
    float *north_east;
    float *south_east;
    float *south_west;
    float *north_west;
    float *centre;
} t_speed;

typedef unsigned char uchar;

/*
** function prototypes
*/

/* load params, allocate memory, load obstacles & initialise fluid particle densities */
int initialise(const char* paramfile,   const char* obstaclefile,
               t_param* params,         t_speed** cells_ptr,
               t_speed** tmp_cells_ptr, uchar** obstacles_ptr,   float** av_vels_ptr);







// Replacing the whole of main with one continuous for loop to allow for cache optimisation.
float loop_fusion(t_speed* cells, t_speed* tmp_cells, const t_param params, uchar* obstacles, const float c_sq, 
    const float w0, const float w1, const float w2, float accelerate_flow_w1, float accelerate_flow_w2, int chunk_size);
int write_values (const t_param params, t_speed* cells, uchar* obstacles, float* av_vels);










/* finalise, including freeing up allocated memory */
int finalise(const t_param* params,   t_speed** cells_ptr,
             t_speed** tmp_cells_ptr, uchar** obstacles_ptr,   float** av_vels_ptr);

/* Sum all the densities in the grid.
** The total should remain constant from one timestep to the next. */
double total_density(const t_param params, t_speed* cells);

/* calculate Reynolds number */
double calc_reynolds(const t_param params, t_speed* cells, uchar* obstacles);

/* utility functions */
void die(const char* message, const int line, const char* file);
void usage(const char* exe);

/*
** main program:
** initialise, timestep loop, finalise
*/
int main(int argc, char* argv[]) {
    
    // Beaureaucracies
    printf("FFFFF\n");
    char*    paramfile    = NULL;     /* name of the input parameter file */
    char*    obstaclefile = NULL;     /* name of a the input obstacle file */
    t_param  params;                  /* struct to hold parameter values */
    
    // Timing beaureaucracies
    
    struct timeval timstr;            /* structure to hold elapsed time */
    struct rusage ru;                 /* structure to hold CPU time--system and user */
    double tic, toc;                  /* floating point numbers to calculate elapsed wallclock time */
    double usrtim;                    /* floating point number to record elapsed user CPU time */
    double systim;                    /* floating point number to record elapsed system CPU time */
    
    // Relevant data for speed up
  
    t_speed* cells        = NULL;     /* grid containing fluid densities */
    t_speed* tmp_cells    = NULL;     /* scratch space */
    uchar*     obstacles    = NULL;     /* grid indicating which cells are blocked */
    float*  av_vels       = NULL;    /* a record of the av. velocity computed for each timestep */

    // Beaureaucracies: (parse input file)
    if (argc != 3) {
        usage(argv[0]);
    } else {
        paramfile = argv[1];
        obstaclefile = argv[2];
    }

    // initialise ADT load values from file
    initialise(paramfile, obstaclefile, &params, &cells, &tmp_cells, &obstacles, &av_vels);

    // Beaureaucracies
    gettimeofday(&timstr, NULL);
    tic = timstr.tv_sec + (timstr.tv_usec / 1000000.0);
    
    
    
    
    
    const float c_sq = 1.0f / 3.0f; 
    const float w0   = 4.0f / 9.0f; 
    const float w1   = 1.0f / 9.0f; 
    const float w2   = 1.0f / 36.0f;
    float accelerate_flow_w1 = params.density * params.accel / 9.0f;
    float accelerate_flow_w2 = accelerate_flow_w1 * 0.25f;
    int chunk_size = params.ny/NUM_THREADS;
    
    // iterate for maxIters timesteps (Normally like 80000 times)

    
    for (int tt = 0; tt < params.maxIters; tt++) {
    
        av_vels[tt] = loop_fusion(cells, tmp_cells, params, obstacles, c_sq, w0, w1, w2, accelerate_flow_w1, accelerate_flow_w2, chunk_size);
        
        t_speed* tmp_pointer = NULL;
        tmp_pointer = cells;
        cells       = tmp_cells;
        tmp_cells   = tmp_pointer;

        #ifdef DEBUG
            printf("==timestep: %d==\n", tt);
            printf("av velocity: %.12E\n", av_vels[tt]);
            printf("tot density: %.12E\n", total_density(params, cells));
        #endif
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // End of timing Beauracracies
    
    gettimeofday(&timstr, NULL);
    toc = timstr.tv_sec + (timstr.tv_usec / 1000000.0);
    getrusage(RUSAGE_SELF, &ru);
    timstr = ru.ru_utime;
    usrtim = timstr.tv_sec + (timstr.tv_usec / 1000000.0);
    timstr = ru.ru_stime;
    systim = timstr.tv_sec + (timstr.tv_usec / 1000000.0);

    // write final values and free memory
    
    printf("==done==\n");
    printf("Reynolds number:\t\t%.12E\n", calc_reynolds(params, cells, obstacles));
    printf("Elapsed time:\t\t\t%.6lf (s)\n", toc - tic);
    printf("Elapsed user CPU time:\t\t%.6lf (s)\n", usrtim);
    printf("Elapsed system CPU time:\t%.6lf (s)\n", systim);
    write_values(params, cells, obstacles, av_vels);
    finalise(&params, &cells, &tmp_cells, &obstacles, &av_vels);

    return EXIT_SUCCESS;
}


float loop_fusion(t_speed* cells, t_speed* tmp_cells, const t_param params, uchar* obstacles, const float c_sq, 
    const float w0, const float w1, const float w2, float accelerate_flow_w1, float accelerate_flow_w2, int chunk_size) {

    int accelerate_flow_row   = params.ny - 2;

    // @Param: av_velocity
    int    tot_cells = 0;   // no. of cells used in calculation
    double tot_u     = 0.0;

    #pragma omp parallel for schedule( static, chunk_size ) num_threads(16)
    for (int jj = 0; jj < params.nx; jj++) {

        // Begin with accelerate flow:
        
        // if the cell is not occupied and we don't send a negative density
        

        if (!obstacles[accelerate_flow_row * params.nx + jj]
            && (cells[0].west[accelerate_flow_row * params.nx + jj] - accelerate_flow_w1) > 0.0
            && (cells[0].north_west[accelerate_flow_row * params.nx + jj] - accelerate_flow_w2) > 0.0
            && (cells[0].south_west[accelerate_flow_row * params.nx + jj] - accelerate_flow_w2) > 0.0) {

            
            cells[0].east[accelerate_flow_row * params.nx + jj] += accelerate_flow_w1;
            cells[0].north_east[accelerate_flow_row * params.nx + jj] += accelerate_flow_w2;
            cells[0].south_east[accelerate_flow_row * params.nx + jj] += accelerate_flow_w2;
            
            cells[0].west[accelerate_flow_row * params.nx + jj] -= accelerate_flow_w1;
            cells[0].north_west[accelerate_flow_row * params.nx + jj] -= accelerate_flow_w2;
            cells[0].south_west[accelerate_flow_row * params.nx + jj] -= accelerate_flow_w2;
        }
    }

    #pragma omp parallel for simd schedule( static, chunk_size ) reduction (+:tot_cells, tot_u) num_threads(16)
    #pragma loop count (256)
    for (int ii = 0; ii < params.ny; ii++) {
        #pragma loop count (256)
        #pragma ivdep
        #pragma vector aligned
        for (int jj = 0; jj < params.nx; jj++) {

            // Propagate is here:

            /* determine indices of axis-direction neighbours
             ** respecting periodic boundary conditions (wrap around) */

            int y_n = (ii + 1) % params.ny;
            int x_e = (jj + 1) % params.nx;
            int y_s = (ii == 0) ? (ii + params.ny - 1) : (ii - 1);
            int x_w = (jj == 0) ? (jj + params.nx - 1) : (jj - 1);

            /* propagate densities to neighbouring cells, following
             ** appropriate directions of travel and writing into
             ** scratch space grid */

            uchar ob_val = obstacles[ii * params.nx + jj];

            float t0 = cells[0].centre    [ii * params.nx + jj  ]; 
            float t1 = cells[0].east      [ii * params.nx + x_w ];
            float t2 = cells[0].north     [y_s * params.nx + jj ];
            float t3 = cells[0].west      [ii * params.nx + x_e ];
            float t4 = cells[0].south     [y_n * params.nx + jj ];
            float t5 = cells[0].north_east[y_s * params.nx + x_w];
            float t6 = cells[0].north_west[y_s * params.nx + x_e];
            float t7 = cells[0].south_west[y_n * params.nx + x_e];
            float t8 = cells[0].south_east[y_n * params.nx + x_w];

            /* compute local density total */
            float local_density = 0.0f;

            local_density += t0;
            local_density += t1;
            local_density += t2;
            local_density += t3;
            local_density += t4;
            local_density += t5;
            local_density += t6;
            local_density += t7;
            local_density += t8;

            float save_space = 1.0f/local_density;

            /* compute x velocity component */
            float u_x = (t1
                          + t5
                          + t8
                          - (t3
                             + t6
                             + t7))
            
                    * save_space;
            
            /* compute y velocity component */
            float u_y = (t2
                          + t5
                          + t6
                          - (t4
                             +t7
                             + t8))
            
                    * save_space;

            /* velocity squared */
            float u_sq = u_x * u_x + u_y * u_y;

            /* equilibrium densities */
            float d_equ[NSPEEDS];

            float y_exp  = 1.0f/c_sq;
            float y_exp2 = y_exp * 0.5f;
            float y_exp3 = y_exp2 * y_exp;

            float repeated_density_w1 = w1 * local_density;
            float repeated_density_w2 = w2 * local_density;

            d_equ[0] = w0 * local_density  * (1.0f - (u_sq * y_exp2));
            d_equ[1] = repeated_density_w1 * (((1.0f + (u_x * y_exp))  + ((u_x * u_x) * (y_exp3))) - (u_sq*(y_exp2)));
            d_equ[2] = repeated_density_w1 * (((1.0f + (u_y * y_exp))  + ((u_y * u_y) * (y_exp3))) - (u_sq*(y_exp2)));
            d_equ[3] = repeated_density_w1 * (((1.0f + (-(u_x) * y_exp)) + ((u_x * u_x) * (y_exp3))) - (u_sq*(y_exp2)));
            d_equ[4] = repeated_density_w1 * (((1.0f + (-(u_y) * y_exp)) + ((u_y * u_y) * (y_exp3))) - (u_sq*(y_exp2)));

            float u_5_squared = (u_x*u_x) + (u_y*u_y) + (2.0f*u_x*u_y);
            float u_6_squared = (u_x*u_x) + (u_y*u_y) - (2.0f*u_x*u_y);
            float u_7_squared = u_5_squared;
            float u_8_squared = u_6_squared;
            d_equ[5] = repeated_density_w2 * (((1.0f + ((u_x + u_y) * y_exp))       + ((u_5_squared) * (y_exp3))) - (u_sq*(y_exp2)));
            d_equ[6] = repeated_density_w2 * (((1.0f + ((-(u_x) + u_y) * y_exp))    + ((u_6_squared) * (y_exp3))) - (u_sq*(y_exp2)));
            d_equ[7] = repeated_density_w2 * (((1.0f + ((-(u_x) - (u_y)) * y_exp))  + ((u_7_squared) * (y_exp3))) - (u_sq*(y_exp2)));
            d_equ[8] = repeated_density_w2 * (((1.0f + ((u_x - (u_y)) * y_exp))     + ((u_8_squared) * (y_exp3))) - (u_sq*(y_exp2)));


            float x0 = (1-ob_val) ? (t0 + params.omega * (d_equ[0] - t0)) : t0;

            float x1 = (1-ob_val) ? (t1 + params.omega * (d_equ[1] - t1)) : t3;
            
            float x2 = (1-ob_val) ? (t2 + params.omega * (d_equ[2] - t2)) : t4;
            
            float x3 = (1-ob_val) ? (t3 + params.omega * (d_equ[3] - t3)) : t1;
            
            float x4 = (1-ob_val) ? (t4 + params.omega * (d_equ[4] - t4)) : t2;
            
            float x5 = (1-ob_val) ? (t5 + params.omega * (d_equ[5] - t5)) : t7;
            
            float x6 = (1-ob_val) ? (t6 + params.omega * (d_equ[6] - t6)) : t8;
            
            float x7 = (1-ob_val) ? (t7 + params.omega * (d_equ[7] - t7)) : t5;
            
            float x8 = (1-ob_val) ? (t8 + params.omega * (d_equ[8] - t8)) : t6;
            
            tot_u += (1-ob_val)*sqrt((u_x * u_x) + (u_y * u_y));
            tot_cells += (1-ob_val);

            tmp_cells[0].centre    [ii * params.nx + jj] = x0;
            tmp_cells[0].east      [ii * params.nx + jj] = x1;
            tmp_cells[0].north     [ii * params.nx + jj] = x2;
            tmp_cells[0].west      [ii * params.nx + jj] = x3;
            tmp_cells[0].south     [ii * params.nx + jj] = x4;
            tmp_cells[0].north_east[ii * params.nx + jj] = x5;
            tmp_cells[0].north_west[ii * params.nx + jj] = x6;
            tmp_cells[0].south_west[ii * params.nx + jj] = x7;
            tmp_cells[0].south_east[ii * params.nx + jj] = x8;
        }
    }

    return tot_u / (float)tot_cells;
}

int initialise(const char* paramfile, const char* obstaclefile,
               t_param* params, t_speed** cells_ptr, t_speed** tmp_cells_ptr,
               uchar** obstacles_ptr, float** av_vels_ptr)
{
  char   message[1024];  /* message buffer */
  FILE*   fp;            /* file pointer */
  int    xx, yy;         /* generic array indices */
  int    blocked;        /* indicates whether a cell is blocked by an obstacle */
  int    retval;         /* to hold return value for checking */

  /* open the parameter file */
  fp = fopen(paramfile, "r");

  if (fp == NULL)
  {
    sprintf(message, "could not open input parameter file: %s", paramfile);
    die(message, __LINE__, __FILE__);
  }

  /* read in the parameter values */
  retval = fscanf(fp, "%d\n", &(params->nx));

  if (retval != 1) die("could not read param file: nx", __LINE__, __FILE__);

  retval = fscanf(fp, "%d\n", &(params->ny));

  if (retval != 1) die("could not read param file: ny", __LINE__, __FILE__);

  retval = fscanf(fp, "%d\n", &(params->maxIters));

  if (retval != 1) die("could not read param file: maxIters", __LINE__, __FILE__);

  retval = fscanf(fp, "%d\n", &(params->reynolds_dim));

  if (retval != 1) die("could not read param file: reynolds_dim", __LINE__, __FILE__);

  retval = fscanf(fp, "%f\n", &(params->density));

  if (retval != 1) die("could not read param file: density", __LINE__, __FILE__);

  retval = fscanf(fp, "%f\n", &(params->accel));

  if (retval != 1) die("could not read param file: accel", __LINE__, __FILE__);

  retval = fscanf(fp, "%f\n", &(params->omega));

  if (retval != 1) die("could not read param file: omega", __LINE__, __FILE__);

  /* and close up the file */
  fclose(fp);

  /*
  ** Allocate memory.
  **
  ** Remember C is pass-by-value, so we need to
  ** pass pointers into the initialise function.
  **
  ** NB we are allocating a 1D array, so that the
  ** memory will be contiguous.  We still want to
  ** index this memory as if it were a (row major
  ** ordered) 2D array, however.  We will perform
  ** some arithmetic using the row and column
  ** coordinates, inside the square brackets, when
  ** we want to access elements of this array.
  **
  ** Note also that we are using a structure to
  ** hold an array of 'speeds'.  We will allocate
  ** a 1D array of these structs.
  */

  /* main grid */

  printf("TTT\n");

  *cells_ptr = (t_speed*)malloc(sizeof(t_speed));

  int size_of_array          = (params->nx)*(params->ny);
  
  (*cells_ptr)[0].north      = (float*)malloc(sizeof(float)*size_of_array);
  (*cells_ptr)[0].south      = (float*)malloc(sizeof(float)*size_of_array);
  (*cells_ptr)[0].east       = (float*)malloc(sizeof(float)*size_of_array);
  (*cells_ptr)[0].west       = (float*)malloc(sizeof(float)*size_of_array);
  (*cells_ptr)[0].north_east = (float*)malloc(sizeof(float)*size_of_array);
  (*cells_ptr)[0].south_east = (float*)malloc(sizeof(float)*size_of_array);
  (*cells_ptr)[0].south_west = (float*)malloc(sizeof(float)*size_of_array);
  (*cells_ptr)[0].north_west = (float*)malloc(sizeof(float)*size_of_array);
  (*cells_ptr)[0].centre     = (float*)malloc(sizeof(float)*size_of_array);

  if (*cells_ptr == NULL) die("cannot allocate memory for cells", __LINE__, __FILE__);

  /* 'helper' grid, used as scratch space */
  *tmp_cells_ptr = (t_speed*)malloc(sizeof(t_speed));

  (*tmp_cells_ptr)[0].north      = (float*)malloc(sizeof(float)*size_of_array);
  (*tmp_cells_ptr)[0].south      = (float*)malloc(sizeof(float)*size_of_array);
  (*tmp_cells_ptr)[0].east       = (float*)malloc(sizeof(float)*size_of_array);
  (*tmp_cells_ptr)[0].west       = (float*)malloc(sizeof(float)*size_of_array);
  (*tmp_cells_ptr)[0].north_east = (float*)malloc(sizeof(float)*size_of_array);
  (*tmp_cells_ptr)[0].south_east = (float*)malloc(sizeof(float)*size_of_array);
  (*tmp_cells_ptr)[0].south_west = (float*)malloc(sizeof(float)*size_of_array);
  (*tmp_cells_ptr)[0].north_west = (float*)malloc(sizeof(float)*size_of_array);
  (*tmp_cells_ptr)[0].centre     = (float*)malloc(sizeof(float)*size_of_array);

  if (*tmp_cells_ptr == NULL) die("cannot allocate memory for tmp_cells", __LINE__, __FILE__);

  /* the map of obstacles */
  *obstacles_ptr = malloc(sizeof(uchar) * (params->ny * params->nx));

  if (*obstacles_ptr == NULL) die("cannot allocate column memory for obstacles", __LINE__, __FILE__);

  /* initialise densities */
  float w0      = params->density * 4.0f / 9.0f;
  float w1      = params->density       / 9.0f;
  float w2      = params->density       / 36.0f;
  int chunk_size = params->ny/NUM_THREADS;


  #pragma omp parallel for simd schedule( static, chunk_size ) num_threads(16)
  for (int ii = 0; ii < params->ny; ii++)
  {
    #pragma ivdep
    for (int jj = 0; jj < params->nx; jj++)
    {
      
      (*cells_ptr)[0].centre    [ii * params->nx + jj] = w0;
      
      (*cells_ptr)[0].east      [ii * params->nx + jj] = w1;
      (*cells_ptr)[0].west      [ii * params->nx + jj] = w1;
      (*cells_ptr)[0].north     [ii * params->nx + jj] = w1;
      (*cells_ptr)[0].south     [ii * params->nx + jj] = w1;
      
      (*cells_ptr)[0].north_east[ii * params->nx + jj] = w2;
      (*cells_ptr)[0].north_west[ii * params->nx + jj] = w2;
      (*cells_ptr)[0].south_west[ii * params->nx + jj] = w2;
      (*cells_ptr)[0].south_east[ii * params->nx + jj] = w2;
    }
  }

  #pragma omp parallel for simd schedule( static, chunk_size ) num_threads(16)
  /* first set all cells in obstacle array to zero */
  for (int ii = 0; ii < params->ny; ii++)
  {
    #pragma ivdep
    for (int jj = 0; jj < params->nx; jj++)
    {
      (*obstacles_ptr)[ii * params->nx + jj] = 0;
    }
  }

  /* open the obstacle data file */
  fp = fopen(obstaclefile, "r");

  if (fp == NULL)
  {
    sprintf(message, "could not open input obstacles file: %s", obstaclefile);
    die(message, __LINE__, __FILE__);
  }

  /* read-in the blocked cells list */
  while ((retval = fscanf(fp, "%d %d %d\n", &xx, &yy, &blocked)) != EOF)
  {
    /* some checks */
    if (retval != 3) die("expected 3 values per line in obstacle file", __LINE__, __FILE__);

    if (xx < 0 || xx > params->nx - 1) die("obstacle x-coord out of range", __LINE__, __FILE__);

    if (yy < 0 || yy > params->ny - 1) die("obstacle y-coord out of range", __LINE__, __FILE__);

    if (blocked != 1) die("obstacle blocked value should be 1", __LINE__, __FILE__);

    /* assign to array */
    (*obstacles_ptr)[yy * params->nx + xx] = blocked;
  }

  /* and close the file */
  fclose(fp);

  /*
  ** allocate space to hold a record of the avarage velocities computed
  ** at each timestep
  */
  *av_vels_ptr = (float*)malloc(sizeof(float) * params->maxIters);

  return EXIT_SUCCESS;
}

int finalise(const t_param* params, t_speed** cells_ptr, t_speed** tmp_cells_ptr,
             uchar** obstacles_ptr, float** av_vels_ptr)
{
  /*
  ** free up allocated memory
  */
  free((*cells_ptr)[0].north);   
  free((*cells_ptr)[0].south);     
  free((*cells_ptr)[0].east);      
  free((*cells_ptr)[0].west);      
  free((*cells_ptr)[0].north_east);
  free((*cells_ptr)[0].south_east);
  free((*cells_ptr)[0].south_west);
  free((*cells_ptr)[0].north_west);
  free((*cells_ptr)[0].centre); 

  (*cells_ptr)[0].north = NULL;
  (*cells_ptr)[0].south = NULL;
  (*cells_ptr)[0].east = NULL;
  (*cells_ptr)[0].west = NULL;
  (*cells_ptr)[0].north_east = NULL;
  (*cells_ptr)[0].south_east = NULL;
  (*cells_ptr)[0].south_west = NULL;
  (*cells_ptr)[0].north_west = NULL;
  (*cells_ptr)[0].centre = NULL;

  free((*tmp_cells_ptr)[0].north);   
  free((*tmp_cells_ptr)[0].south);     
  free((*tmp_cells_ptr)[0].east);      
  free((*tmp_cells_ptr)[0].west);      
  free((*tmp_cells_ptr)[0].north_east);
  free((*tmp_cells_ptr)[0].south_east);
  free((*tmp_cells_ptr)[0].south_west);
  free((*tmp_cells_ptr)[0].north_west);
  free((*tmp_cells_ptr)[0].centre); 

  (*tmp_cells_ptr)[0].north = NULL;
  (*tmp_cells_ptr)[0].south = NULL;
  (*tmp_cells_ptr)[0].east = NULL;
  (*tmp_cells_ptr)[0].west = NULL;
  (*tmp_cells_ptr)[0].north_east = NULL;
  (*tmp_cells_ptr)[0].south_east = NULL;
  (*tmp_cells_ptr)[0].south_west = NULL;
  (*tmp_cells_ptr)[0].north_west = NULL;
  (*tmp_cells_ptr)[0].centre = NULL;

  free(*cells_ptr);
  *cells_ptr = NULL;

  free(*tmp_cells_ptr);
  *tmp_cells_ptr = NULL;

  free(*obstacles_ptr);
  *obstacles_ptr = NULL;

  free(*av_vels_ptr);
  *av_vels_ptr = NULL;

  return EXIT_SUCCESS;
}


double calc_reynolds(const t_param params, t_speed* cells, uchar* obstacles) {
    
 // Manually inline av_velocity.

    const double viscosity = 1.0 / 6.0 * (2.0 / params.omega - 1.0);
    int    tot_cells       = 0;   // no. of cells used in calculation
    double tot_u;           // accumulated magnitudes of velocity for each cell

    // initialise
    tot_u = 0.0;

    int chunk_size = params.ny/NUM_THREADS;

    /* loop over all non-blocked cells */
    #pragma omp parallel for simd reduction (+:tot_cells, tot_u) schedule( static, chunk_size ) num_threads(16)
    for (int ii = 0; ii < params.ny; ii++) {
        for (int jj = 0; jj < params.nx; jj++) {

            uchar ob_val = obstacles[ii * params.nx + jj];
            
            float local_density = 0.0;

            local_density += cells[0].east      [ii * params.nx + jj];
            local_density += cells[0].west      [ii * params.nx + jj];
            local_density += cells[0].north     [ii * params.nx + jj];
            local_density += cells[0].south     [ii * params.nx + jj];
            local_density += cells[0].north_east[ii * params.nx + jj];
            local_density += cells[0].north_west[ii * params.nx + jj];
            local_density += cells[0].south_east[ii * params.nx + jj];
            local_density += cells[0].south_west[ii * params.nx + jj];
            local_density += cells[0].centre    [ii * params.nx + jj];

            float save_space = 1.0/local_density;

            float u_x = (cells[0].east[ii * params.nx + jj]
                          + cells[0].north_east[ii * params.nx + jj]
                          + cells[0].south_east[ii * params.nx + jj]
                          - (cells[0].west      [ii * params.nx + jj]
                             + cells[0].north_west[ii * params.nx + jj]
                             + cells[0].south_west[ii * params.nx + jj]))
            
                    * save_space;
            
            
            float u_y = (cells[0].north     [ii * params.nx + jj]
                          + cells[0].north_east[ii * params.nx + jj]
                          + cells[0].north_west[ii * params.nx + jj]
                          - (cells[0].south     [ii * params.nx + jj]
                             + cells[0].south_west[ii * params.nx + jj]
                             + cells[0].south_east[ii * params.nx + jj]))
                    * save_space;
        
            tot_u += (1-ob_val)*sqrt((u_x * u_x) + (u_y * u_y));
            tot_cells += (1-ob_val);


        }
    }

    double av_velocity = tot_u / (double)tot_cells;

  return av_velocity * params.reynolds_dim / viscosity;
}

double total_density(const t_param params, t_speed* cells)
{
  double total = 0.0;  /* accumulator */

  for (int ii = 0; ii < params.ny; ii++)
  {
    for (int jj = 0; jj < params.nx; jj++)
    {
        total += cells[0].east      [ii * params.nx + jj];
        total += cells[0].west      [ii * params.nx + jj];
        total += cells[0].north     [ii * params.nx + jj];
        total += cells[0].south     [ii * params.nx + jj];
        total += cells[0].north_east[ii * params.nx + jj];
        total += cells[0].north_west[ii * params.nx + jj];
        total += cells[0].south_east[ii * params.nx + jj];
        total += cells[0].south_west[ii * params.nx + jj];
        total += cells[0].centre    [ii * params.nx + jj];
    }
  }

  return total;
}

int write_values(const t_param params, t_speed* cells, uchar* obstacles, float* av_vels)
{
  FILE* fp;                     /* file pointer */
  const double c_sq = 1.0 / 3.0; /* sq. of speed of sound */
  float local_density;         /* per grid cell sum of densities */
  float pressure;              /* fluid pressure in grid cell */
  float u_x;                   /* x-component of velocity in grid cell */
  float u_y;                   /* y-component of velocity in grid cell */
  float u;                     /* norm--root of summed squares--of u_x and u_y */

  fp = fopen(FINALSTATEFILE, "w");

  if (fp == NULL)
  {
    die("could not open file output file", __LINE__, __FILE__);
  }

  for (int ii = 0; ii < params.ny; ii++)
  {
    for (int jj = 0; jj < params.nx; jj++)
    {
      /* an occupied cell */
      if (obstacles[ii * params.nx + jj])
      {
        u_x = u_y = u = 0.0;
        pressure = params.density * c_sq;
      }
      /* no obstacle */
      else
      {
        local_density = 0.0;

        local_density += cells[0].east      [ii * params.nx + jj];
        local_density += cells[0].west      [ii * params.nx + jj];
        local_density += cells[0].north     [ii * params.nx + jj];
        local_density += cells[0].south     [ii * params.nx + jj];
        local_density += cells[0].north_east[ii * params.nx + jj];
        local_density += cells[0].north_west[ii * params.nx + jj];
        local_density += cells[0].south_east[ii * params.nx + jj];
        local_density += cells[0].south_west[ii * params.nx + jj];
        local_density += cells[0].centre    [ii * params.nx + jj];

        /* compute x velocity component */
        u_x = (cells[0].east[ii * params.nx + jj]
               + cells[0].north_east[ii * params.nx + jj]
               + cells[0].south_east[ii * params.nx + jj]
               - (cells[0].west      [ii * params.nx + jj]
                  + cells[0].north_west[ii * params.nx + jj]
                  + cells[0].south_west[ii * params.nx + jj]))
              / local_density;
        /* compute y velocity component */
        u_y = (cells[0].north     [ii * params.nx + jj]
               + cells[0].north_east[ii * params.nx + jj]
               + cells[0].north_west[ii * params.nx + jj]
               - (cells[0].south     [ii * params.nx + jj]
                  + cells[0].south_west[ii * params.nx + jj]
                  + cells[0].south_east[ii * params.nx + jj]))
              / local_density;
        /* compute norm of velocity */
        u = sqrt((u_x * u_x) + (u_y * u_y));
        /* compute pressure */
        pressure = local_density * c_sq;
      }

      /* write to file */
      fprintf(fp, "%d %d %.12E %.12E %.12E %.12E %d\n", jj, ii, u_x, u_y, u, pressure, obstacles[ii * params.nx + jj]);
    }
  }

  fclose(fp);

  fp = fopen(AVVELSFILE, "w");

  if (fp == NULL)
  {
    die("could not open file output file", __LINE__, __FILE__);
  }

  for (int ii = 0; ii < params.maxIters; ii++)
  {
    fprintf(fp, "%d:\t%.12E\n", ii, av_vels[ii]);
  }

  fclose(fp);

  return EXIT_SUCCESS;
}

void die(const char* message, const int line, const char* file)
{
  fprintf(stderr, "Error at line %d of file %s:\n", line, file);
  fprintf(stderr, "%s\n", message);
  fflush(stderr);
  exit(EXIT_FAILURE);
}

void usage(const char* exe)
{
  fprintf(stderr, "Usage: %s <paramfile> <obstaclefile>\n", exe);
  exit(EXIT_FAILURE);
}

