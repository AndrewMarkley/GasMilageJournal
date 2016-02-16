using GasMilageJournal.Models.Interfaces;
using Microsoft.Data.Entity;
using System;
using System.Threading.Tasks;

namespace GasMilageJournal.Extensions
{
    public static class DataContextExtensions
    {
        /// <summary>
        /// Adds or Updates an entity based on whether the Id of type System.Guid is provided
        /// </summary>
        /// <typeparam name="T">The type of entity to add or update.</typeparam>
        /// <param name="dataContext">The DbContext this extension method is being added to.</param>
        /// <param name="entity">The entity to be added or updated.</param>
        /// <returns>The entity resulting from the SaveChangesAsync operation.</returns>
        public static async Task<T> AddOrUpdateAsync<T>(this DbContext dataContext, T entity) where T : class, IBaseModel<Guid>
        {
            return await dataContext.AddOrUpdateAsync<T, Guid>(entity);
        }

        /// <summary>
        /// Adds or Updates an entity based on whether the Id of generic type TKey is provided
        /// </summary>
        /// <typeparam name="T">The type of entity to add or update.</typeparam>
        /// <typeparam name="TKey">The type of the Id of the entity</typeparam>
        /// <param name="dataContext">The DbContext this extension method is being added to.</param>
        /// <param name="entity">The entity to be added or updated.</param>
        /// <returns>The entity resulting from the SaveChangesAsync operation.</returns>
        public static async Task<T> AddOrUpdateAsync<T, TKey>(this DbContext dataContext, T entity) where T : class, IBaseModel<TKey>
        {
            if (entity == null) {
                return default(T);
            }

            if (entity.Id == null) {
                dataContext.Add(entity);
            } else {
                dataContext.Update(entity);
            }

            await dataContext.SaveChangesAsync();

            return entity;
        }
    }
}
