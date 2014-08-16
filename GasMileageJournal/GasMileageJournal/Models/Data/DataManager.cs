using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Threading.Tasks;

namespace GasMileageJournal.Models.Data
{
    public class DataManager<T> : IDisposable where T : class
    {
        public DataContext Context = new DataContext();

        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }

        public T GetById(String id)
        {
            return GetById<T>(id);
        }

        public TU GetById<TU>(String id) where TU : class
        {
            return Context.Set<TU>().Find(id);
        }

        public async Task<T> GetByIdAsync(String id)
        {
            return await GetByIdAsync<T>(id);
        }

        public async Task<TU> GetByIdAsync<TU>(String id) where TU : class
        {
            return await Context.Set<TU>().FindAsync(id);
        }

        public List<T> GetAll()
        {
            return GetAll<T>();
        }

        public List<TU> GetAll<TU>() where TU : class
        {
            var result = Context.Set<TU>();

            if (result == null) {
                return null;
            }

            return result.ToList();
        }

        public T Find(Expression<Func<T, bool>> match)
        {
            return Find<T>(match);
        }

        public TU Find<TU>(Expression<Func<TU, bool>> match) where TU : class
        {
            return Context.Set<TU>().SingleOrDefault(match);
        }

        public List<T> FindAll(Expression<Func<T, bool>> match)
        {
            return FindAll<T>(match);
        }

        public List<TU> FindAll<TU>(Expression<Func<TU, bool>> match) where TU : class
        {
            return Context.Set<TU>().Where(match).ToList();
        }

        public int Count()
        {
            return Count<T>();
        }

        public int Count<TU>() where TU : class
        {
            return Context.Set<TU>().Count();
        }

        public DeleteResult Delete(String id)
        {
            return Delete<T>(id);
        }

        public DeleteResult Delete<TU>(String id) where TU : class
        {
            try {
                var entity = GetById<TU>(id);

                if (entity == null) {
                    return DeleteResult.NotFound;
                }

                Context.Set<TU>().Remove(entity);

                Context.SaveChanges();
            } catch (Exception) {
                return DeleteResult.Error;
            }

            return DeleteResult.Success;
        }

        public String Save(T data)
        {
            return Save<T>(data);
        }

        public String Save<TU>(TU data) where TU : class
        {
            try {
                var dataModel = (IDataModel)data;

                var entity = (IDataModel)GetById<TU>(dataModel.Id);

                if (entity == null) {
                    Context.Set<TU>().Add(data);
                } else {
                    Context.Entry(entity).CurrentValues.SetValues(dataModel);
                }

                Context.SaveChanges();

                return dataModel.Id;
            } catch (Exception) {
                Console.WriteLine("Error in Type: {0}", typeof(TU));
                return null;
            }
        }

        // The bulk of the clean-up code is implemented in Dispose(bool)
        protected virtual void Dispose(bool disposing)
        {
            if (disposing) {
                // free managed resources
                if (Context != null) {
                    Context.Dispose();
                    Context = null;
                }
            }
        }
    }
}