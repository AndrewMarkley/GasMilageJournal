using Microsoft.AspNet.Mvc;
using System;
using System.Collections.Generic;
using System.Net;

namespace GasMilageJournal.Services.Interfaces
{
    public class ServiceResult : ObjectResult
    {
        public bool HasError { get; set; } = false;
        public List<string> Messages { get; set; } = new List<string>();

        public ServiceResult()
            : base(null)
        {
            StatusCode = (int)(HttpStatusCode.OK);
        }

        public ServiceResult(object obj)
            : base(obj)
        {
            StatusCode = (int)(HttpStatusCode.OK);
        }

        public ServiceResult(object obj, HttpStatusCode status)
            : base(obj)
        {
            StatusCode = (int)(status);
        }

        public ServiceResult(Exception ex)
            : base(ex)
        {
            StatusCode = (int)(HttpStatusCode.InternalServerError);
            HasError = true;

            while (ex.InnerException != null) {
                ex = ex.InnerException;
            }

            Messages.Add(ex.InnerException.ToString());
        }

        public ServiceResult(Exception ex, HttpStatusCode status)
            : base(ex)
        {
            StatusCode = (int)(status);
            HasError = true;

            while (ex.InnerException != null) {
                ex = ex.InnerException;
            }

            Messages.Add(ex.InnerException.ToString());
        }
    }
}
